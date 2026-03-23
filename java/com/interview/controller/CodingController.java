package com.interview.controller;

import com.interview.dto.CodingDTO;
import com.interview.service.CodingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coding")
public class CodingController {

    private final CodingService codingService;

    public CodingController(CodingService codingService) {
        this.codingService = codingService;
    }

    @GetMapping("/questions")
    public ResponseEntity<List<CodingDTO.QuestionResponse>> getQuestions(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(codingService.getAllActiveQuestions(userDetails.getUsername()));
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<CodingDTO.QuestionResponse> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(codingService.getQuestionById(id));
    }

    @PostMapping("/submit")
    public ResponseEntity<CodingDTO.SubmissionResponse> submitSolution(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CodingDTO.SubmitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(codingService.submitSolution(userDetails.getUsername(), request));
    }

    @GetMapping("/submissions")
    public ResponseEntity<List<CodingDTO.SubmissionResponse>> getMySubmissions(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(codingService.getCandidateSubmissions(userDetails.getUsername()));
    }
}
