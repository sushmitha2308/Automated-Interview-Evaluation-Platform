package com.interview.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "video_responses")
public class VideoResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private User candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private InterviewQuestion question;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(nullable = false, length = 255)
    private String originalFileName;

    @Column(nullable = false, length = 50)
    private String contentType;

    @Column(nullable = false)
    private long fileSizeBytes;

    @Column
    private Integer durationSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String recruiterNotes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    private LocalDateTime reviewedAt;

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }

    public enum Status {
        UPLOADED, UNDER_REVIEW, REVIEWED, FLAGGED
    }

    // Constructors
    public VideoResponse() {}

    // Getters
    public Long getId() { return id; }
    public User getCandidate() { return candidate; }
    public InterviewQuestion getQuestion() { return question; }
    public String getFilePath() { return filePath; }
    public String getOriginalFileName() { return originalFileName; }
    public String getContentType() { return contentType; }
    public long getFileSizeBytes() { return fileSizeBytes; }
    public Integer getDurationSeconds() { return durationSeconds; }
    public Status getStatus() { return status; }
    public Integer getRating() { return rating; }
    public String getRecruiterNotes() { return recruiterNotes; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setCandidate(User candidate) { this.candidate = candidate; }
    public void setQuestion(InterviewQuestion question) { this.question = question; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }
    public void setContentType(String contentType) { this.contentType = contentType; }
    public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
    public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
    public void setStatus(Status status) { this.status = status; }
    public void setRating(Integer rating) { this.rating = rating; }
    public void setRecruiterNotes(String recruiterNotes) { this.recruiterNotes = recruiterNotes; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private User candidate;
        private InterviewQuestion question;
        private String filePath;
        private String originalFileName;
        private String contentType;
        private long fileSizeBytes;
        private Integer durationSeconds;
        private Status status;
        private Integer rating;
        private String recruiterNotes;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder candidate(User candidate) { this.candidate = candidate; return this; }
        public Builder question(InterviewQuestion question) { this.question = question; return this; }
        public Builder filePath(String filePath) { this.filePath = filePath; return this; }
        public Builder originalFileName(String originalFileName) { this.originalFileName = originalFileName; return this; }
        public Builder contentType(String contentType) { this.contentType = contentType; return this; }
        public Builder fileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; return this; }
        public Builder durationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; return this; }
        public Builder status(Status status) { this.status = status; return this; }
        public Builder rating(Integer rating) { this.rating = rating; return this; }
        public Builder recruiterNotes(String recruiterNotes) { this.recruiterNotes = recruiterNotes; return this; }

        public VideoResponse build() {
            VideoResponse v = new VideoResponse();
            v.id = this.id;
            v.candidate = this.candidate;
            v.question = this.question;
            v.filePath = this.filePath;
            v.originalFileName = this.originalFileName;
            v.contentType = this.contentType;
            v.fileSizeBytes = this.fileSizeBytes;
            v.durationSeconds = this.durationSeconds;
            v.status = this.status;
            v.rating = this.rating;
            v.recruiterNotes = this.recruiterNotes;
            return v;
        }
    }
}
