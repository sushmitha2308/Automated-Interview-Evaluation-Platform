package com.interview.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coding_questions")
public class CodingQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(columnDefinition = "TEXT")
    private String sampleInput;

    @Column(columnDefinition = "TEXT")
    private String sampleOutput;

    @Column(columnDefinition = "TEXT")
    private String constraints;

    @Column(nullable = false)
    private int totalMarks = 100;

    @Column(nullable = false)
    private int timeLimitMinutes = 60;

    @Column(nullable = false)
    private boolean active = true;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TestCase> testCases = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CodingSubmission> submissions = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    // Constructors
    public CodingQuestion() {}

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Difficulty getDifficulty() { return difficulty; }
    public String getSampleInput() { return sampleInput; }
    public String getSampleOutput() { return sampleOutput; }
    public String getConstraints() { return constraints; }
    public int getTotalMarks() { return totalMarks; }
    public int getTimeLimitMinutes() { return timeLimitMinutes; }
    public boolean isActive() { return active; }
    public List<TestCase> getTestCases() { return testCases; }
    public List<CodingSubmission> getSubmissions() { return submissions; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDifficulty(Difficulty difficulty) { this.difficulty = difficulty; }
    public void setSampleInput(String sampleInput) { this.sampleInput = sampleInput; }
    public void setSampleOutput(String sampleOutput) { this.sampleOutput = sampleOutput; }
    public void setConstraints(String constraints) { this.constraints = constraints; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }
    public void setTimeLimitMinutes(int timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; }
    public void setActive(boolean active) { this.active = active; }
    public void setTestCases(List<TestCase> testCases) { this.testCases = testCases; }
    public void setSubmissions(List<CodingSubmission> submissions) { this.submissions = submissions; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String title;
        private String description;
        private Difficulty difficulty;
        private String sampleInput;
        private String sampleOutput;
        private String constraints;
        private int totalMarks = 100;
        private int timeLimitMinutes = 60;
        private boolean active = true;
        private List<TestCase> testCases = new ArrayList<>();
        private List<CodingSubmission> submissions = new ArrayList<>();

        public Builder id(Long id) { this.id = id; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder difficulty(Difficulty difficulty) { this.difficulty = difficulty; return this; }
        public Builder sampleInput(String sampleInput) { this.sampleInput = sampleInput; return this; }
        public Builder sampleOutput(String sampleOutput) { this.sampleOutput = sampleOutput; return this; }
        public Builder constraints(String constraints) { this.constraints = constraints; return this; }
        public Builder totalMarks(int totalMarks) { this.totalMarks = totalMarks; return this; }
        public Builder timeLimitMinutes(int timeLimitMinutes) { this.timeLimitMinutes = timeLimitMinutes; return this; }
        public Builder active(boolean active) { this.active = active; return this; }
        public Builder testCases(List<TestCase> testCases) { this.testCases = testCases; return this; }
        public Builder submissions(List<CodingSubmission> submissions) { this.submissions = submissions; return this; }

        public CodingQuestion build() {
            CodingQuestion q = new CodingQuestion();
            q.id = this.id;
            q.title = this.title;
            q.description = this.description;
            q.difficulty = this.difficulty;
            q.sampleInput = this.sampleInput;
            q.sampleOutput = this.sampleOutput;
            q.constraints = this.constraints;
            q.totalMarks = this.totalMarks;
            q.timeLimitMinutes = this.timeLimitMinutes;
            q.active = this.active;
            q.testCases = this.testCases;
            q.submissions = this.submissions;
            return q;
        }
    }
}
