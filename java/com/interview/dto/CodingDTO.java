package com.interview.dto;

import com.interview.entity.CodingSubmission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CodingDTO {

    public static class SubmitRequest {
        @NotNull(message = "Question ID is required")
        private Long questionId;

        @NotBlank(message = "Code is required")
        private String code;

        @NotNull(message = "Language is required")
        private CodingSubmission.Language language;

        public SubmitRequest() {}

        public SubmitRequest(Long questionId, String code, CodingSubmission.Language language) {
            this.questionId = questionId;
            this.code = code;
            this.language = language;
        }

        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public CodingSubmission.Language getLanguage() { return language; }
        public void setLanguage(CodingSubmission.Language language) { this.language = language; }
    }

    public static class SubmissionResponse {
        private Long id;
        private Long questionId;
        private String questionTitle;
        private String code;
        private CodingSubmission.Language language;
        private CodingSubmission.Status status;
        private int score;
        private int testCasesPassed;
        private int totalTestCases;
        private String feedback;
        private Long executionTimeMs;
        private LocalDateTime submittedAt;

        public SubmissionResponse() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public String getQuestionTitle() { return questionTitle; }
        public void setQuestionTitle(String questionTitle) { this.questionTitle = questionTitle; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public CodingSubmission.Language getLanguage() { return language; }
        public void setLanguage(CodingSubmission.Language language) { this.language = language; }
        public CodingSubmission.Status getStatus() { return status; }
        public void setStatus(CodingSubmission.Status status) { this.status = status; }
        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }
        public int getTestCasesPassed() { return testCasesPassed; }
        public void setTestCasesPassed(int testCasesPassed) { this.testCasesPassed = testCasesPassed; }
        public int getTotalTestCases() { return totalTestCases; }
        public void setTotalTestCases(int totalTestCases) { this.totalTestCases = totalTestCases; }
        public String getFeedback() { return feedback; }
        public void setFeedback(String feedback) { this.feedback = feedback; }
        public Long getExecutionTimeMs() { return executionTimeMs; }
        public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
        public LocalDateTime getSubmittedAt() { return submittedAt; }
        public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final SubmissionResponse obj = new SubmissionResponse();
            public Builder id(Long id) { obj.id = id; return this; }
            public Builder questionId(Long questionId) { obj.questionId = questionId; return this; }
            public Builder questionTitle(String questionTitle) { obj.questionTitle = questionTitle; return this; }
            public Builder code(String code) { obj.code = code; return this; }
            public Builder language(CodingSubmission.Language language) { obj.language = language; return this; }
            public Builder status(CodingSubmission.Status status) { obj.status = status; return this; }
            public Builder score(int score) { obj.score = score; return this; }
            public Builder testCasesPassed(int testCasesPassed) { obj.testCasesPassed = testCasesPassed; return this; }
            public Builder totalTestCases(int totalTestCases) { obj.totalTestCases = totalTestCases; return this; }
            public Builder feedback(String feedback) { obj.feedback = feedback; return this; }
            public Builder executionTimeMs(Long executionTimeMs) { obj.executionTimeMs = executionTimeMs; return this; }
            public Builder submittedAt(LocalDateTime submittedAt) { obj.submittedAt = submittedAt; return this; }
            public SubmissionResponse build() { return obj; }
        }
    }

    public static class QuestionResponse {
        private Long id;
        private String title;
        private String description;
        private String difficulty;
        private String sampleInput;
        private String sampleOutput;
        private String constraints;
        private int totalMarks;
        private int timeLimitMinutes;
        private boolean submitted;

        public QuestionResponse() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDifficulty() { return difficulty; }
        public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
        public String getSampleInput() { return sampleInput; }
        public void setSampleInput(String sampleInput) { this.sampleInput = sampleInput; }
        public String getSampleOutput() { return sampleOutput; }
        public void setSampleOutput(String sampleOutput) { this.sampleOutput = sampleOutput; }
        public String getConstraints() { return constraints; }
        public void setConstraints(String constraints) { this.constraints = constraints; }
        public int getTotalMarks() { return totalMarks; }
        public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }
        public int getTimeLimitMinutes() { return timeLimitMinutes; }
        public void setTimeLimitMinutes(int timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }
        public boolean isSubmitted() { return submitted; }
        public void setSubmitted(boolean submitted) { this.submitted = submitted; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final QuestionResponse obj = new QuestionResponse();
            public Builder id(Long id) { obj.id = id; return this; }
            public Builder title(String title) { obj.title = title; return this; }
            public Builder description(String description) { obj.description = description; return this; }
            public Builder difficulty(String difficulty) { obj.difficulty = difficulty; return this; }
            public Builder sampleInput(String sampleInput) { obj.sampleInput = sampleInput; return this; }
            public Builder sampleOutput(String sampleOutput) { obj.sampleOutput = sampleOutput; return this; }
            public Builder constraints(String constraints) { obj.constraints = constraints; return this; }
            public Builder totalMarks(int totalMarks) { obj.totalMarks = totalMarks; return this; }
            public Builder timeLimitMinutes(int timeLimitMinutes) { obj.timeLimitMinutes = timeLimitMinutes; return this; }
            public Builder submitted(boolean submitted) { obj.submitted = submitted; return this; }
            public QuestionResponse build() { return obj; }
        }
    }
}
