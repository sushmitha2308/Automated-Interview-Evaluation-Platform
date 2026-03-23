package com.interview.service;

import com.interview.dto.CodingDTO;
import com.interview.entity.*;
import com.interview.exception.ResourceNotFoundException;
import com.interview.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class CodingService {

    private static final Logger log = LoggerFactory.getLogger(CodingService.class);

    private final CodingQuestionRepository questionRepository;
    private final CodingSubmissionRepository submissionRepository;
    private final TestCaseRepository testCaseRepository;
    private final UserRepository userRepository;

    public CodingService(CodingQuestionRepository questionRepository,
                         CodingSubmissionRepository submissionRepository,
                         TestCaseRepository testCaseRepository,
                         UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.submissionRepository = submissionRepository;
        this.testCaseRepository = testCaseRepository;
        this.userRepository = userRepository;
    }

    private static final List<Pattern> DANGEROUS_PATTERNS = List.of(
        Pattern.compile("Runtime\\.getRuntime\\(\\)\\.exec", Pattern.CASE_INSENSITIVE),
        Pattern.compile("ProcessBuilder", Pattern.CASE_INSENSITIVE),
        Pattern.compile("System\\.exit", Pattern.CASE_INSENSITIVE),
        Pattern.compile("import\\s+java\\.io\\.File", Pattern.CASE_INSENSITIVE),
        Pattern.compile("FileWriter|FileReader|Files\\.", Pattern.CASE_INSENSITIVE),
        Pattern.compile("java\\.net\\.", Pattern.CASE_INSENSITIVE),
        Pattern.compile("__import__\\s*\\(\\s*['\"]os['\"]", Pattern.CASE_INSENSITIVE),
        Pattern.compile("subprocess", Pattern.CASE_INSENSITIVE),
        Pattern.compile("eval\\s*\\(", Pattern.CASE_INSENSITIVE),
        Pattern.compile("exec\\s*\\(", Pattern.CASE_INSENSITIVE)
    );

    public List<CodingDTO.QuestionResponse> getAllActiveQuestions(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return questionRepository.findByActiveTrue().stream().map(q -> {
            boolean submitted = submissionRepository
                    .findByCandidateIdAndQuestionId(user.getId(), q.getId()).isPresent();
            return CodingDTO.QuestionResponse.builder()
                    .id(q.getId())
                    .title(q.getTitle())
                    .description(q.getDescription())
                    .difficulty(q.getDifficulty().name())
                    .sampleInput(q.getSampleInput())
                    .sampleOutput(q.getSampleOutput())
                    .constraints(q.getConstraints())
                    .totalMarks(q.getTotalMarks())
                    .timeLimitMinutes(q.getTimeLimitMinutes())
                    .submitted(submitted)
                    .build();
        }).toList();
    }

    public CodingDTO.QuestionResponse getQuestionById(Long id) {
        CodingQuestion question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found: " + id));
        return CodingDTO.QuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .description(question.getDescription())
                .difficulty(question.getDifficulty().name())
                .sampleInput(question.getSampleInput())
                .sampleOutput(question.getSampleOutput())
                .constraints(question.getConstraints())
                .totalMarks(question.getTotalMarks())
                .timeLimitMinutes(question.getTimeLimitMinutes())
                .build();
    }

    @Transactional
    public CodingDTO.SubmissionResponse submitSolution(String userEmail, CodingDTO.SubmitRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CodingQuestion question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        if (isMaliciousCode(request.getCode())) {
            CodingSubmission flagged = CodingSubmission.builder()
                    .candidate(user)
                    .question(question)
                    .code(request.getCode())
                    .language(request.getLanguage())
                    .status(CodingSubmission.Status.COMPILE_ERROR)
                    .score(0)
                    .feedback("Submission flagged: potentially unsafe code detected.")
                    .totalTestCases(0)
                    .testCasesPassed(0)
                    .build();
            submissionRepository.save(flagged);
            return toSubmissionResponse(flagged);
        }

        List<TestCase> testCases = testCaseRepository.findByQuestionId(question.getId());
        ScoringResult eval = evaluateSubmission(request.getCode(), request.getLanguage(), testCases, question);

        CodingSubmission submission = CodingSubmission.builder()
                .candidate(user)
                .question(question)
                .code(request.getCode())
                .language(request.getLanguage())
                .status(eval.passed > 0 ? (eval.passed == testCases.size() ?
                        CodingSubmission.Status.PASSED : CodingSubmission.Status.FAILED) :
                        CodingSubmission.Status.FAILED)
                .score(eval.score)
                .testCasesPassed(eval.passed)
                .totalTestCases(testCases.size())
                .feedback(generateFeedback(eval, testCases.size()))
                .executionTimeMs(eval.executionTime)
                .build();

        submissionRepository.save(submission);
        return toSubmissionResponse(submission);
    }

    public List<CodingDTO.SubmissionResponse> getCandidateSubmissions(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return submissionRepository.findByCandidateOrderBySubmittedAtDesc(user)
                .stream().map(this::toSubmissionResponse).toList();
    }

    private boolean isMaliciousCode(String code) {
        for (Pattern pattern : DANGEROUS_PATTERNS) {
            if (pattern.matcher(code).find()) {
                log.warn("Potentially malicious code detected");
                return true;
            }
        }
        return false;
    }

    private ScoringResult evaluateSubmission(String code, CodingSubmission.Language lang,
                                              List<TestCase> testCases, CodingQuestion question) {
        int passed = 0;
        int totalMarks = 0;
        long startTime = System.currentTimeMillis();

        for (TestCase tc : testCases) {
            boolean pass = simulateTestCaseExecution(code, lang, tc.getInput(), tc.getExpectedOutput());
            if (pass) {
                passed++;
                totalMarks += tc.getMarks();
            }
        }

        long executionTime = System.currentTimeMillis() - startTime;
        int maxMarks = question.getTotalMarks();
        int score = testCases.isEmpty() ? 0 : (int) Math.round(((double) totalMarks / testCases.stream()
                .mapToInt(TestCase::getMarks).sum()) * maxMarks);
        score = Math.min(maxMarks, score + calculateEfficiencyBonus(code, lang, score));

        return new ScoringResult(passed, score, executionTime);
    }

    private boolean simulateTestCaseExecution(String code, CodingSubmission.Language lang,
                                               String input, String expectedOutput) {
        if (code == null || code.trim().isEmpty()) return false;
        String codeLC = code.toLowerCase();
        boolean hasLogic = codeLC.contains("return") || codeLC.contains("print") ||
                           codeLC.contains("system.out") || codeLC.contains("console.log");
        boolean hasStructure = code.length() > 50;
        return hasLogic && hasStructure && (Math.random() > 0.3);
    }

    private int calculateEfficiencyBonus(String code, CodingSubmission.Language lang, int baseScore) {
        if (baseScore < 50) return 0;
        int bonus = 0;
        if (!code.contains("for") || !code.matches("(?s).*for.*for.*")) bonus += 3;
        if (code.length() < 500) bonus += 2;
        return bonus;
    }

    private String generateFeedback(ScoringResult eval, int total) {
        double percentage = total == 0 ? 0 : (double) eval.passed / total * 100;
        if (percentage == 100) return "Excellent! All test cases passed.";
        if (percentage >= 75) return String.format("Good attempt! %d/%d test cases passed.", eval.passed, total);
        if (percentage >= 50) return String.format("Partial solution. %d/%d test cases passed. Review edge cases.", eval.passed, total);
        return String.format("Needs improvement. Only %d/%d test cases passed.", eval.passed, total);
    }

    private CodingDTO.SubmissionResponse toSubmissionResponse(CodingSubmission s) {
        return CodingDTO.SubmissionResponse.builder()
                .id(s.getId())
                .questionId(s.getQuestion().getId())
                .questionTitle(s.getQuestion().getTitle())
                .code(s.getCode())
                .language(s.getLanguage())
                .status(s.getStatus())
                .score(s.getScore())
                .testCasesPassed(s.getTestCasesPassed())
                .totalTestCases(s.getTotalTestCases())
                .feedback(s.getFeedback())
                .executionTimeMs(s.getExecutionTimeMs())
                .submittedAt(s.getSubmittedAt())
                .build();
    }

    private static class ScoringResult {
        int passed;
        int score;
        long executionTime;

        ScoringResult(int passed, int score, long executionTime) {
            this.passed = passed;
            this.score = score;
            this.executionTime = executionTime;
        }
    }
}
