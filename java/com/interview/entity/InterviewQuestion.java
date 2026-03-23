package com.interview.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_questions")
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String questionText;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int maxDurationSeconds = 180;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false)
    private int orderIndex = 0;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VideoResponse> videoResponses = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Constructors
    public InterviewQuestion() {}

    // Getters
    public Long getId() { return id; }
    public String getQuestionText() { return questionText; }
    public String getDescription() { return description; }
    public int getMaxDurationSeconds() { return maxDurationSeconds; }
    public boolean isActive() { return active; }
    public int getOrderIndex() { return orderIndex; }
    public List<VideoResponse> getVideoResponses() { return videoResponses; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public void setDescription(String description) { this.description = description; }
    public void setMaxDurationSeconds(int maxDurationSeconds) { this.maxDurationSeconds = maxDurationSeconds; }
    public void setActive(boolean active) { this.active = active; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
    public void setVideoResponses(List<VideoResponse> videoResponses) { this.videoResponses = videoResponses; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String questionText;
        private String description;
        private int maxDurationSeconds = 180;
        private boolean active = true;
        private int orderIndex = 0;
        private List<VideoResponse> videoResponses = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder questionText(String questionText) { this.questionText = questionText; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder maxDurationSeconds(int maxDurationSeconds) { this.maxDurationSeconds = maxDurationSeconds; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder orderIndex(int orderIndex) { this.orderIndex = orderIndex; return this; }
        public Builder videoResponses(List<VideoResponse> videoResponses) { this.videoResponses = videoResponses; return this; }

        public InterviewQuestion build() {
            InterviewQuestion q = new InterviewQuestion();
            q.id = this.id;
            q.questionText = this.questionText;
            q.description = this.description;
            q.maxDurationSeconds = this.maxDurationSeconds;
            q.active = this.active;
            q.orderIndex = this.orderIndex;
            q.videoResponses = this.videoResponses;
            return q;
        }
    }
}
