package com.interview.controller;

import com.interview.entity.InterviewQuestion;
import com.interview.repository.InterviewQuestionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interview-questions")
public class InterviewQuestionController {

    private final InterviewQuestionRepository questionRepository;

    public InterviewQuestionController(InterviewQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @GetMapping
    public ResponseEntity<List<InterviewQuestion>> getActiveQuestions() {
        return ResponseEntity.ok(questionRepository.findByActiveTrueOrderByOrderIndex());
    }
}
