package com.interview.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coding_submissions")
public class CodingSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private CodingQuestion question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Language language;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private int score = 0;

    @Column(nullable = false)
    private int testCasesPassed = 0;

    @Column(nullable = false)
    private int totalTestCases = 0;

    @Column(columnDefinition = "TEXT")
    private String feedback;

    @Column
    private Long executionTimeMs;

    @Column
    private Long memoryUsedKb;

    @Column(nullable = false, updatable = false)
    private LocalDateTime submittedAt;

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }

    public enum Language {
        JAVA, PYTHON, JAVASCRIPT, CPP, C
    }

    public enum Status {
        PENDING, EVALUATING, PASSED, FAILED, COMPILE_ERROR, RUNTIME_ERROR, TIME_LIMIT_EXCEEDED
    }

    // Constructors
    public CodingSubmission() {}

    // Getters
    public Long getId() { return id; }
    public User getCandidate() { return candidate; }
    public CodingQuestion getQuestion() { return question; }
    public String getCode() { return code; }
    public Language getLanguage() { return language; }
    public Status getStatus() { return status; }
    public int getScore() { return score; }
    public int getTestCasesPassed() { return testCasesPassed; }
    public int getTotalTestCases() { return totalTestCases; }
    public String getFeedback() { return feedback; }
    public Long getExecutionTimeMs() { return executionTimeMs; }
    public Long getMemoryUsedKb() { return memoryUsedKb; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCandidate(User candidate) { this.candidate = candidate; }
    public void setQuestion(CodingQuestion question) { this.question = question; }
    public void setCode(String code) { this.code = code; }
    public void setLanguage(Language language) { this.language = language; }
    public void setStatus(Status status) { this.status = status; }
    public void setScore(int score) { this.score = score; }
    public void setTestCasesPassed(int testCasesPassed) { this.testCasesPassed = testCasesPassed; }
    public void setTotalTestCases(int totalTestCases) { this.totalTestCases = totalTestCases; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public void setExecutionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; }
    public void setMemoryUsedKb(Long memoryUsedKb) { this.memoryUsedKb = memoryUsedKb; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User candidate;
        private CodingQuestion question;
        private String code;
        private Language language;
        private Status status;
        private int score = 0;
        private int testCasesPassed = 0;
        private int totalTestCases = 0;
        private String feedback;
        private Long executionTimeMs;
        private Long memoryUsedKb;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder candidate(User candidate) { this.candidate = candidate; return this; }
        public Builder question(CodingQuestion question) { this.question = question; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder language(Language language) { this.language = language; return this; }
        public Builder status(Status status) { this.status = status; return this; }
        public Builder score(int score) { this.score = score; return this; }
        public Builder testCasesPassed(int testCasesPassed) { this.testCasesPassed = testCasesPassed; return this; }
        public Builder totalTestCases(int totalTestCases) { this.totalTestCases = totalTestCases; return this; }
        public Builder feedback(String feedback) { this.feedback = feedback; return this; }
        public Builder executionTimeMs(Long executionTimeMs) { this.executionTimeMs = executionTimeMs; return this; }
        public Builder memoryUsedKb(Long memoryUsedKb) { this.memoryUsedKb = memoryUsedKb; return this; }

        public CodingSubmission build() {
            CodingSubmission s = new CodingSubmission();
            s.id = this.id;
            s.candidate = this.candidate;
            s.question = this.question;
            s.code = this.code;
            s.language = this.language;
            s.status = this.status;
            s.score = this.score;
            s.testCasesPassed = this.testCasesPassed;
            s.totalTestCases = this.totalTestCases;
            s.feedback = this.feedback;
            s.executionTimeMs = this.executionTimeMs;
            s.memoryUsedKb = this.memoryUsedKb;
            return s;
        }
    }
}
