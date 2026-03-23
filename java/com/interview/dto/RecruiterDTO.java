package com.interview.dto;

import com.interview.entity.EvaluationResult;
import java.time.LocalDateTime;
import java.util.List;

public class RecruiterDTO {

    public static class CandidateSummary {
        private Long candidateId;
        private String fullName;
        private String email;
        private String phone;
        private double codingScore;
        private double videoScore;
        private double overallScore;
        private int codingSubmissions;
        private int videoResponses;
        private EvaluationResult.Status status;
        private LocalDateTime joinedAt;

        public CandidateSummary() {}

        public Long getCandidateId() { return candidateId; }
        public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public double getCodingScore() { return codingScore; }
        public void setCodingScore(double codingScore) { this.codingScore = codingScore; }
        public double getVideoScore() { return videoScore; }
        public void setVideoScore(double videoScore) { this.videoScore = videoScore; }
        public double getOverallScore() { return overallScore; }
        public void setOverallScore(double overallScore) { this.overallScore = overallScore; }
        public int getCodingSubmissions() { return codingSubmissions; }
        public void setCodingSubmissions(int codingSubmissions) { this.codingSubmissions = codingSubmissions; }
        public int getVideoResponses() { return videoResponses; }
        public void setVideoResponses(int videoResponses) { this.videoResponses = videoResponses; }
        public EvaluationResult.Status getStatus() { return status; }
        public void setStatus(EvaluationResult.Status status) { this.status = status; }
        public LocalDateTime getJoinedAt() { return joinedAt; }
        public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final CandidateSummary obj = new CandidateSummary();
            public Builder candidateId(Long candidateId) { obj.candidateId = candidateId; return this; }
            public Builder fullName(String fullName) { obj.fullName = fullName; return this; }
            public Builder email(String email) { obj.email = email; return this; }
            public Builder phone(String phone) { obj.phone = phone; return this; }
            public Builder codingScore(double codingScore) { obj.codingScore = codingScore; return this; }
            public Builder videoScore(double videoScore) { obj.videoScore = videoScore; return this; }
            public Builder overallScore(double overallScore) { obj.overallScore = overallScore; return this; }
            public Builder codingSubmissions(int codingSubmissions) { obj.codingSubmissions = codingSubmissions; return this; }
            public Builder videoResponses(int videoResponses) { obj.videoResponses = videoResponses; return this; }
            public Builder status(EvaluationResult.Status status) { obj.status = status; return this; }
            public Builder joinedAt(LocalDateTime joinedAt) { obj.joinedAt = joinedAt; return this; }
            public CandidateSummary build() { return obj; }
        }
    }

    public static class CandidateDetail {
        private Long candidateId;
        private String fullName;
        private String email;
        private String phone;
        private double overallScore;
        private EvaluationResult.Status status;
        private List<CodingDTO.SubmissionResponse> codingSubmissions;
        private List<VideoDTO.VideoDetailResponse> videoResponses;
        private String summaryNotes;

        public CandidateDetail() {}

        public Long getCandidateId() { return candidateId; }
        public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public double getOverallScore() { return overallScore; }
        public void setOverallScore(double overallScore) { this.overallScore = overallScore; }
        public EvaluationResult.Status getStatus() { return status; }
        public void setStatus(EvaluationResult.Status status) { this.status = status; }
        public List<CodingDTO.SubmissionResponse> getCodingSubmissions() { return codingSubmissions; }
        public void setCodingSubmissions(List<CodingDTO.SubmissionResponse> codingSubmissions) { this.codingSubmissions = codingSubmissions; }
        public List<VideoDTO.VideoDetailResponse> getVideoResponses() { return videoResponses; }
        public void setVideoResponses(List<VideoDTO.VideoDetailResponse> videoResponses) { this.videoResponses = videoResponses; }
        public String getSummaryNotes() { return summaryNotes; }
        public void setSummaryNotes(String summaryNotes) { this.summaryNotes = summaryNotes; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final CandidateDetail obj = new CandidateDetail();
            public Builder candidateId(Long candidateId) { obj.candidateId = candidateId; return this; }
            public Builder fullName(String fullName) { obj.fullName = fullName; return this; }
            public Builder email(String email) { obj.email = email; return this; }
            public Builder phone(String phone) { obj.phone = phone; return this; }
            public Builder overallScore(double overallScore) { obj.overallScore = overallScore; return this; }
            public Builder status(EvaluationResult.Status status) { obj.status = status; return this; }
            public Builder codingSubmissions(List<CodingDTO.SubmissionResponse> codingSubmissions) { obj.codingSubmissions = codingSubmissions; return this; }
            public Builder videoResponses(List<VideoDTO.VideoDetailResponse> videoResponses) { obj.videoResponses = videoResponses; return this; }
            public Builder summaryNotes(String summaryNotes) { obj.summaryNotes = summaryNotes; return this; }
            public CandidateDetail build() { return obj; }
        }
    }

    public static class DashboardStats {
        private long totalCandidates;
        private long pendingReviews;
        private long shortlisted;
        private long rejected;
        private double avgOverallScore;
        private long totalSubmissions;
        private long totalVideoUploads;

        public DashboardStats() {}

        public long getTotalCandidates() { return totalCandidates; }
        public void setTotalCandidates(long totalCandidates) { this.totalCandidates = totalCandidates; }
        public long getPendingReviews() { return pendingReviews; }
        public void setPendingReviews(long pendingReviews) { this.pendingReviews = pendingReviews; }
        public long getShortlisted() { return shortlisted; }
        public void setShortlisted(long shortlisted) { this.shortlisted = shortlisted; }
        public long getRejected() { return rejected; }
        public void setRejected(long rejected) { this.rejected = rejected; }
        public double getAvgOverallScore() { return avgOverallScore; }
        public void setAvgOverallScore(double avgOverallScore) { this.avgOverallScore = avgOverallScore; }
        public long getTotalSubmissions() { return totalSubmissions; }
        public void setTotalSubmissions(long totalSubmissions) { this.totalSubmissions = totalSubmissions; }
        public long getTotalVideoUploads() { return totalVideoUploads; }
        public void setTotalVideoUploads(long totalVideoUploads) { this.totalVideoUploads = totalVideoUploads; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final DashboardStats obj = new DashboardStats();
            public Builder totalCandidates(long totalCandidates) { obj.totalCandidates = totalCandidates; return this; }
            public Builder pendingReviews(long pendingReviews) { obj.pendingReviews = pendingReviews; return this; }
            public Builder shortlisted(long shortlisted) { obj.shortlisted = shortlisted; return this; }
            public Builder rejected(long rejected) { obj.rejected = rejected; return this; }
            public Builder avgOverallScore(double avgOverallScore) { obj.avgOverallScore = avgOverallScore; return this; }
            public Builder totalSubmissions(long totalSubmissions) { obj.totalSubmissions = totalSubmissions; return this; }
            public Builder totalVideoUploads(long totalVideoUploads) { obj.totalVideoUploads = totalVideoUploads; return this; }
            public DashboardStats build() { return obj; }
        }
    }

    public static class UpdateStatusRequest {
        private EvaluationResult.Status status;
        private String summaryNotes;

        public UpdateStatusRequest() {}

        public EvaluationResult.Status getStatus() { return status; }
        public void setStatus(EvaluationResult.Status status) { this.status = status; }
        public String getSummaryNotes() { return summaryNotes; }
        public void setSummaryNotes(String summaryNotes) { this.summaryNotes = summaryNotes; }
    }
}
