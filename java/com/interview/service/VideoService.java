package com.interview.service;

import com.interview.dto.VideoDTO;
import com.interview.entity.*;
import com.interview.exception.InvalidRequestException;
import com.interview.exception.ResourceNotFoundException;
import com.interview.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class VideoService {

    private static final Logger log = LoggerFactory.getLogger(VideoService.class);

    private final VideoResponseRepository videoResponseRepository;
    private final InterviewQuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Value("${file.upload.video-dir}")
    private String videoUploadDir;

    @Value("${file.upload.allowed-video-extensions}")
    private String allowedExtensions;

    @Value("${file.upload.max-video-size}")
    private long maxVideoSize;

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
        "video/mp4", "video/webm", "video/avi", "video/quicktime", "video/x-msvideo"
    );

    public VideoService(VideoResponseRepository videoResponseRepository,
                        InterviewQuestionRepository questionRepository,
                        UserRepository userRepository) {
        this.videoResponseRepository = videoResponseRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VideoDTO.UploadResponse uploadVideo(String userEmail, Long questionId, MultipartFile file) {
        User candidate = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        InterviewQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found: " + questionId));

        validateVideoFile(file);

        String storedFileName = saveFile(file, candidate.getId(), questionId);

        VideoResponse videoResponse = VideoResponse.builder()
                .candidate(candidate)
                .question(question)
                .filePath(storedFileName)
                .originalFileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .fileSizeBytes(file.getSize())
                .status(VideoResponse.Status.UPLOADED)
                .build();

        videoResponseRepository.save(videoResponse);

        return VideoDTO.UploadResponse.builder()
                .id(videoResponse.getId())
                .questionId(questionId)
                .questionText(question.getQuestionText())
                .originalFileName(file.getOriginalFilename())
                .fileSizeBytes(file.getSize())
                .status(VideoResponse.Status.UPLOADED)
                .uploadedAt(videoResponse.getUploadedAt())
                .build();
    }

    public List<VideoDTO.VideoDetailResponse> getCandidateVideos(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return videoResponseRepository.findByCandidateIdOrderByUploadedAtDesc(user.getId())
                .stream().map(v -> toDetailResponse(v, true)).toList();
    }

    @Transactional
    public VideoDTO.VideoDetailResponse reviewVideo(Long videoId, VideoDTO.VideoReviewRequest request) {
        VideoResponse video = videoResponseRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found: " + videoId));

        if (request.getRating() != null) {
            if (request.getRating() < 1 || request.getRating() > 5) {
                throw new InvalidRequestException("Rating must be between 1 and 5");
            }
            video.setRating(request.getRating());
        }
        if (request.getRecruiterNotes() != null) {
            video.setRecruiterNotes(request.getRecruiterNotes());
        }
        if (request.getStatus() != null) {
            video.setStatus(request.getStatus());
        }
        video.setReviewedAt(LocalDateTime.now());
        videoResponseRepository.save(video);

        return toDetailResponse(video, true);
    }

    public Path getVideoFilePath(Long videoId) {
        VideoResponse video = videoResponseRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Video not found"));
        return Paths.get(videoUploadDir).resolve(video.getFilePath());
    }

    private void validateVideoFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidRequestException("File is empty");
        }
        if (file.getSize() > maxVideoSize) {
            throw new InvalidRequestException("File size exceeds maximum allowed size of 100MB");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidRequestException("Invalid file type. Allowed: MP4, WebM, AVI, MOV");
        }
        String filename = file.getOriginalFilename();
        if (filename != null) {
            String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
            List<String> allowed = Arrays.asList(allowedExtensions.split(","));
            if (!allowed.contains(ext)) {
                throw new InvalidRequestException("Invalid file extension");
            }
        }
    }

    private String saveFile(MultipartFile file, Long userId, Long questionId) {
        try {
            Path uploadPath = Paths.get(videoUploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String ext = originalFilename.substring(originalFilename.lastIndexOf('.'));
            String storedFileName = "user_" + userId + "_q_" + questionId + "_" + UUID.randomUUID() + ext;
            Path filePath = uploadPath.resolve(storedFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return storedFileName;
        } catch (IOException e) {
            log.error("File upload failed", e);
            throw new RuntimeException("Failed to store video file", e);
        }
    }

    private VideoDTO.VideoDetailResponse toDetailResponse(VideoResponse v, boolean includeUrl) {
        return VideoDTO.VideoDetailResponse.builder()
                .id(v.getId())
                .candidateId(v.getCandidate().getId())
                .candidateName(v.getCandidate().getFullName())
                .questionId(v.getQuestion().getId())
                .questionText(v.getQuestion().getQuestionText())
                .originalFileName(v.getOriginalFileName())
                .fileSizeBytes(v.getFileSizeBytes())
                .durationSeconds(v.getDurationSeconds())
                .status(v.getStatus())
                .rating(v.getRating())
                .recruiterNotes(v.getRecruiterNotes())
                .uploadedAt(v.getUploadedAt())
                .reviewedAt(v.getReviewedAt())
                .streamUrl(includeUrl ? "/api/videos/stream/" + v.getId() : null)
                .build();
    }
}
