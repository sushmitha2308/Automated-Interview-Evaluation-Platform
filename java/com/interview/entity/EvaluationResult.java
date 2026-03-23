package com.interview.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluation_results")
public class EvaluationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @Column(nullable = false)
    private double totalCodingScore;

    @Column(nullable = false)
    private double totalVideoScore;

    @Column(nullable = false)
    private double overallScore;

    @Column(nullable = false)
    private int codingSubmissionsCount;

    @Column(nullable = false)
    private int videoResponsesCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(columnDefinition = "TEXT")
    private String summaryNotes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime evaluatedAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        evaluatedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Status {
        PENDING, IN_PROGRESS, COMPLETED, SHORTLISTED, REJECTED
    }

    // Constructors
    public EvaluationResult() {}

    // Getters
    public Long getId() { return id; }
    public User getCandidate() { return candidate; }
    public double getTotalCodingScore() { return totalCodingScore; }
    public double getTotalVideoScore() { return totalVideoScore; }
    public double getOverallScore() { return overallScore; }
    public int getCodingSubmissionsCount() { return codingSubmissionsCount; }
    public int getVideoResponsesCount() { return videoResponsesCount; }
    public Status getStatus() { return status; }
    public String getSummaryNotes() { return summaryNotes; }
    public LocalDateTime getEvaluatedAt() { return evaluatedAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCandidate(User candidate) { this.candidate = candidate; }
    public void setTotalCodingScore(double totalCodingScore) { this.totalCodingScore = totalCodingScore; }
    public void setTotalVideoScore(double totalVideoScore) { this.totalVideoScore = totalVideoScore; }
    public void setOverallScore(double overallScore) { this.overallScore = overallScore; }
    public void setCodingSubmissionsCount(int codingSubmissionsCount) { this.codingSubmissionsCount = codingSubmissionsCount; }
    public void setVideoResponsesCount(int videoResponsesCount) { this.videoResponsesCount = videoResponsesCount; }
    public void setStatus(Status status) { this.status = status; }
    public void setSummaryNotes(String summaryNotes) { this.summaryNotes = summaryNotes; }
    public void setEvaluatedAt(LocalDateTime evaluatedAt) { this.evaluatedAt = evaluatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User candidate;
        private double totalCodingScore;
        private double totalVideoScore;
        private double overallScore;
        private int codingSubmissionsCount;
        private int videoResponsesCount;
        private Status status;
        private String summaryNotes;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder candidate(User candidate) { this.candidate = candidate; return this; }
        public Builder totalCodingScore(double totalCodingScore) { this.totalCodingScore = totalCodingScore; return this; }
        public Builder totalVideoScore(double totalVideoScore) { this.totalVideoScore = totalVideoScore; return this; }
        public Builder overallScore(double overallScore) { this.overallScore = overallScore; return this; }
        public Builder codingSubmissionsCount(int codingSubmissionsCount) { this.codingSubmissionsCount = codingSubmissionsCount; return this; }
        public Builder videoResponsesCount(int videoResponsesCount) { this.videoResponsesCount = videoResponsesCount; return this; }
        public Builder status(Status status) { this.status = status; return this; }
        public Builder summaryNotes(String summaryNotes) { this.summaryNotes = summaryNotes; return this; }

        public EvaluationResult build() {
            EvaluationResult e = new EvaluationResult();
            e.id = this.id;
            e.candidate = this.candidate;
            e.totalCodingScore = this.totalCodingScore;
            e.totalVideoScore = this.totalVideoScore;
            e.overallScore = this.overallScore;
            e.codingSubmissionsCount = this.codingSubmissionsCount;
            e.videoResponsesCount = this.videoResponsesCount;
            e.status = this.status;
            e.summaryNotes = this.summaryNotes;
            return e;
        }
    }
}
