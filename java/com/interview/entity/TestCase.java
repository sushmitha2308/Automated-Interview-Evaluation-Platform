package com.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "test_cases")
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private CodingQuestion question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String input;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String expectedOutput;

    @Column(nullable = false)
    private boolean hidden = false;

    @Column(nullable = false)
    private int marks = 10;

    // Constructors
    public TestCase() {}

    // Getters
    public Long getId() { return id; }
    public CodingQuestion getQuestion() { return question; }
    public String getInput() { return input; }
    public String getExpectedOutput() { return expectedOutput; }
    public boolean isHidden() { return hidden; }
    public int getMarks() { return marks; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setQuestion(CodingQuestion question) { this.question = question; }
    public void setInput(String input) { this.input = input; }
    public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }
    public void setMarks(int marks) { this.marks = marks; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private CodingQuestion question;
        private String input;
        private String expectedOutput;
        private boolean hidden = false;
        private int marks = 10;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder question(CodingQuestion question) { this.question = question; return this; }
        public Builder input(String input) { this.input = input; return this; }
        public Builder expectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; return this; }
        public Builder hidden(boolean hidden) { this.hidden = hidden; return this; }
        public Builder marks(int marks) { this.marks = marks; return this; }

        public TestCase build() {
            TestCase t = new TestCase();
            t.id = this.id;
            t.question = this.question;
            t.input = this.input;
            t.expectedOutput = this.expectedOutput;
            t.hidden = this.hidden;
            t.marks = this.marks;
            return t;
        }
    }
}
