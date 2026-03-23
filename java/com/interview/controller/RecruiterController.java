package com.interview.controller;

import com.interview.dto.RecruiterDTO;
import com.interview.service.RecruiterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruiter")
@PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<RecruiterDTO.DashboardStats> getDashboardStats() {
        return ResponseEntity.ok(recruiterService.getDashboardStats());
    }

    @GetMapping("/candidates")
    public ResponseEntity<List<RecruiterDTO.CandidateSummary>> getAllCandidates() {
        return ResponseEntity.ok(recruiterService.getAllCandidates());
    }

    @GetMapping("/candidates/{candidateId}")
    public ResponseEntity<RecruiterDTO.CandidateDetail> getCandidateDetail(@PathVariable Long candidateId) {
        return ResponseEntity.ok(recruiterService.getCandidateDetail(candidateId));
    }

    @PutMapping("/candidates/{candidateId}/status")
    public ResponseEntity<Void> updateCandidateStatus(
            @PathVariable Long candidateId,
            @RequestBody RecruiterDTO.UpdateStatusRequest request) {
        recruiterService.updateCandidateStatus(candidateId, request);
        return ResponseEntity.ok().build();
    }
}
