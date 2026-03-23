package com.interview.dto;

import com.interview.entity.VideoResponse;
import java.time.LocalDateTime;

public class VideoDTO {

    public static class UploadResponse {
        private Long id;
        private Long questionId;
        private String questionText;
        private String originalFileName;
        private long fileSizeBytes;
        private Integer durationSeconds;
        private VideoResponse.Status status;
        private LocalDateTime uploadedAt;

        public UploadResponse() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }
        public String getOriginalFileName() { return originalFileName; }
        public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }
        public long getFileSizeBytes() { return fileSizeBytes; }
        public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
        public Integer getDurationSeconds() { return durationSeconds; }
        public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
        public VideoResponse.Status getStatus() { return status; }
        public void setStatus(VideoResponse.Status status) { this.status = status; }
        public LocalDateTime getUploadedAt() { return uploadedAt; }
        public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final UploadResponse obj = new UploadResponse();
            public Builder id(Long id) { obj.id = id; return this; }
            public Builder questionId(Long questionId) { obj.questionId = questionId; return this; }
            public Builder questionText(String questionText) { obj.questionText = questionText; return this; }
            public Builder originalFileName(String originalFileName) { obj.originalFileName = originalFileName; return this; }
            public Builder fileSizeBytes(long fileSizeBytes) { obj.fileSizeBytes = fileSizeBytes; return this; }
            public Builder durationSeconds(Integer durationSeconds) { obj.durationSeconds = durationSeconds; return this; }
            public Builder status(VideoResponse.Status status) { obj.status = status; return this; }
            public Builder uploadedAt(LocalDateTime uploadedAt) { obj.uploadedAt = uploadedAt; return this; }
            public UploadResponse build() { return obj; }
        }
    }

    public static class VideoReviewRequest {
        private Integer rating;
        private String recruiterNotes;
        private VideoResponse.Status status;

        public VideoReviewRequest() {}

        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
        public String getRecruiterNotes() { return recruiterNotes; }
        public void setRecruiterNotes(String recruiterNotes) { this.recruiterNotes = recruiterNotes; }
        public VideoResponse.Status getStatus() { return status; }
        public void setStatus(VideoResponse.Status status) { this.status = status; }
    }

    public static class VideoDetailResponse {
        private Long id;
        private Long candidateId;
        private String candidateName;
        private Long questionId;
        private String questionText;
        private String originalFileName;
        private long fileSizeBytes;
        private Integer durationSeconds;
        private VideoResponse.Status status;
        private Integer rating;
        private String recruiterNotes;
        private LocalDateTime uploadedAt;
        private LocalDateTime reviewedAt;
        private String streamUrl;

        public VideoDetailResponse() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public Long getCandidateId() { return candidateId; }
        public void setCandidateId(Long candidateId) { this.candidateId = candidateId; }
        public String getCandidateName() { return candidateName; }
        public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }
        public String getQuestionText() { return questionText; }
        public void setQuestionText(String questionText) { this.questionText = questionText; }
        public String getOriginalFileName() { return originalFileName; }
        public void setOriginalFileName(String originalFileName) { this.originalFileName = originalFileName; }
        public long getFileSizeBytes() { return fileSizeBytes; }
        public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }
        public Integer getDurationSeconds() { return durationSeconds; }
        public void setDurationSeconds(Integer durationSeconds) { this.durationSeconds = durationSeconds; }
        public VideoResponse.Status getStatus() { return status; }
        public void setStatus(VideoResponse.Status status) { this.status = status; }
        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
        public String getRecruiterNotes() { return recruiterNotes; }
        public void setRecruiterNotes(String recruiterNotes) { this.recruiterNotes = recruiterNotes; }
        public LocalDateTime getUploadedAt() { return uploadedAt; }
        public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
        public LocalDateTime getReviewedAt() { return reviewedAt; }
        public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
        public String getStreamUrl() { return streamUrl; }
        public void setStreamUrl(String streamUrl) { this.streamUrl = streamUrl; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private final VideoDetailResponse obj = new VideoDetailResponse();
            public Builder id(Long id) { obj.id = id; return this; }
            public Builder candidateId(Long candidateId) { obj.candidateId = candidateId; return this; }
            public Builder candidateName(String candidateName) { obj.candidateName = candidateName; return this; }
            public Builder questionId(Long questionId) { obj.questionId = questionId; return this; }
            public Builder questionText(String questionText) { obj.questionText = questionText; return this; }
            public Builder originalFileName(String originalFileName) { obj.originalFileName = originalFileName; return this; }
            public Builder fileSizeBytes(long fileSizeBytes) { obj.fileSizeBytes = fileSizeBytes; return this; }
            public Builder durationSeconds(Integer durationSeconds) { obj.durationSeconds = durationSeconds; return this; }
            public Builder status(VideoResponse.Status status) { obj.status = status; return this; }
            public Builder rating(Integer rating) { obj.rating = rating; return this; }
            public Builder recruiterNotes(String recruiterNotes) { obj.recruiterNotes = recruiterNotes; return this; }
            public Builder uploadedAt(LocalDateTime uploadedAt) { obj.uploadedAt = uploadedAt; return this; }
            public Builder reviewedAt(LocalDateTime reviewedAt) { obj.reviewedAt = reviewedAt; return this; }
            public Builder streamUrl(String streamUrl) { obj.streamUrl = streamUrl; return this; }
            public VideoDetailResponse build() { return obj; }
        }
    }
}
