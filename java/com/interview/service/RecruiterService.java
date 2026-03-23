package com.interview.service;

import com.interview.dto.CodingDTO;
import com.interview.dto.RecruiterDTO;
import com.interview.dto.VideoDTO;
import com.interview.entity.EvaluationResult;
import com.interview.entity.User;
import com.interview.exception.ResourceNotFoundException;
import com.interview.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecruiterService {

    private final UserRepository userRepository;
    private final CodingSubmissionRepository codingSubmissionRepository;
    private final VideoResponseRepository videoResponseRepository;
    private final EvaluationResultRepository evaluationResultRepository;

    public RecruiterService(UserRepository userRepository,
                            CodingSubmissionRepository codingSubmissionRepository,
                            VideoResponseRepository videoResponseRepository,
                            EvaluationResultRepository evaluationResultRepository) {
        this.userRepository = userRepository;
        this.codingSubmissionRepository = codingSubmissionRepository;
        this.videoResponseRepository = videoResponseRepository;
        this.evaluationResultRepository = evaluationResultRepository;
    }

    public RecruiterDTO.DashboardStats getDashboardStats() {
        long total = userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.CANDIDATE).count();

        return RecruiterDTO.DashboardStats.builder()
                .totalCandidates(total)
                .pendingReviews(evaluationResultRepository.countByStatus(EvaluationResult.Status.PENDING) +
                                evaluationResultRepository.countByStatus(EvaluationResult.Status.IN_PROGRESS))
                .shortlisted(evaluationResultRepository.countByStatus(EvaluationResult.Status.SHORTLISTED))
                .rejected(evaluationResultRepository.countByStatus(EvaluationResult.Status.REJECTED))
                .avgOverallScore(evaluationResultRepository.findAll().stream()
                        .mapToDouble(EvaluationResult::getOverallScore).average().orElse(0.0))
                .totalSubmissions(codingSubmissionRepository.count())
                .totalVideoUploads(videoResponseRepository.count())
                .build();
    }

    public List<RecruiterDTO.CandidateSummary> getAllCandidates() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRole() == User.Role.CANDIDATE)
                .map(this::toCandidateSummary)
                .toList();
    }

    public RecruiterDTO.CandidateDetail getCandidateDetail(Long candidateId) {
        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found: " + candidateId));

        EvaluationResult evalResult = evaluationResultRepository.findByCandidateId(candidateId).orElse(null);

        List<CodingDTO.SubmissionResponse> codingSubmissions = codingSubmissionRepository
                .findByCandidateId(candidateId).stream()
                .map(s -> CodingDTO.SubmissionResponse.builder()
                        .id(s.getId())
                        .questionId(s.getQuestion().getId())
                        .questionTitle(s.getQuestion().getTitle())
                        .language(s.getLanguage())
                        .status(s.getStatus())
                        .score(s.getScore())
                        .testCasesPassed(s.getTestCasesPassed())
                        .totalTestCases(s.getTotalTestCases())
                        .feedback(s.getFeedback())
                        .submittedAt(s.getSubmittedAt())
                        .build())
                .toList();

        List<VideoDTO.VideoDetailResponse> videoResponses = videoResponseRepository
                .findByCandidateIdOrderByUploadedAtDesc(candidateId).stream()
                .map(v -> VideoDTO.VideoDetailResponse.builder()
                        .id(v.getId())
                        .questionId(v.getQuestion().getId())
                        .questionText(v.getQuestion().getQuestionText())
                        .originalFileName(v.getOriginalFileName())
                        .durationSeconds(v.getDurationSeconds())
                        .status(v.getStatus())
                        .rating(v.getRating())
                        .recruiterNotes(v.getRecruiterNotes())
                        .uploadedAt(v.getUploadedAt())
                        .streamUrl("/api/videos/stream/" + v.getId())
                        .build())
                .toList();

        return RecruiterDTO.CandidateDetail.builder()
                .candidateId(candidateId)
                .fullName(candidate.getFullName())
                .email(candidate.getEmail())
                .phone(candidate.getPhone())
                .overallScore(evalResult != null ? evalResult.getOverallScore() : 0)
                .status(evalResult != null ? evalResult.getStatus() : EvaluationResult.Status.PENDING)
                .codingSubmissions(codingSubmissions)
                .videoResponses(videoResponses)
                .summaryNotes(evalResult != null ? evalResult.getSummaryNotes() : null)
                .build();
    }

    @Transactional
    public void updateCandidateStatus(Long candidateId, RecruiterDTO.UpdateStatusRequest request) {
        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found"));

        EvaluationResult evalResult = evaluationResultRepository.findByCandidateId(candidateId)
                .orElseGet(() -> EvaluationResult.builder()
                        .candidate(candidate)
                        .totalCodingScore(0)
                        .totalVideoScore(0)
                        .overallScore(0)
                        .codingSubmissionsCount(0)
                        .videoResponsesCount(0)
                        .status(EvaluationResult.Status.PENDING)
                        .build());

        evalResult.setStatus(request.getStatus());
        if (request.getSummaryNotes() != null) {
            evalResult.setSummaryNotes(request.getSummaryNotes());
        }

        Double avgCoding = codingSubmissionRepository.findAvgScoreByCandidateId(candidateId);
        Double avgVideo = videoResponseRepository.findAvgRatingByCandidateId(candidateId);
        double codingScore = avgCoding != null ? avgCoding : 0;
        double videoScore = avgVideo != null ? (avgVideo / 5.0) * 100 : 0;
        evalResult.setTotalCodingScore(codingScore);
        evalResult.setTotalVideoScore(videoScore);
        evalResult.setOverallScore((codingScore * 0.6) + (videoScore * 0.4));

        int codingCount = codingSubmissionRepository.findByCandidateId(candidateId).size();
        int videoCount = videoResponseRepository.findByCandidateIdOrderByUploadedAtDesc(candidateId).size();
        evalResult.setCodingSubmissionsCount(codingCount);
        evalResult.setVideoResponsesCount(videoCount);

        evaluationResultRepository.save(evalResult);
    }

    private RecruiterDTO.CandidateSummary toCandidateSummary(User candidate) {
        EvaluationResult eval = evaluationResultRepository.findByCandidateId(candidate.getId()).orElse(null);
        Double avgCoding = codingSubmissionRepository.findAvgScoreByCandidateId(candidate.getId());
        Double avgVideo = videoResponseRepository.findAvgRatingByCandidateId(candidate.getId());

        double codingScore = avgCoding != null ? avgCoding : 0;
        double videoScore = avgVideo != null ? (avgVideo / 5.0) * 100 : 0;
        double overall = (codingScore * 0.6) + (videoScore * 0.4);

        return RecruiterDTO.CandidateSummary.builder()
                .candidateId(candidate.getId())
                .fullName(candidate.getFullName())
                .email(candidate.getEmail())
                .phone(candidate.getPhone())
                .codingScore(Math.round(codingScore * 10.0) / 10.0)
                .videoScore(Math.round(videoScore * 10.0) / 10.0)
                .overallScore(Math.round(overall * 10.0) / 10.0)
                .codingSubmissions(codingSubmissionRepository.findByCandidateId(candidate.getId()).size())
                .videoResponses(videoResponseRepository.findByCandidateIdOrderByUploadedAtDesc(candidate.getId()).size())
                .status(eval != null ? eval.getStatus() : EvaluationResult.Status.PENDING)
                .joinedAt(candidate.getCreatedAt())
                .build();
    }
}
