package com.interview.controller;

import com.interview.dto.VideoDTO;
import com.interview.service.VideoService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/upload/{questionId}")
    public ResponseEntity<VideoDTO.UploadResponse> uploadVideo(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long questionId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(videoService.uploadVideo(userDetails.getUsername(), questionId, file));
    }

    @GetMapping("/my-videos")
    public ResponseEntity<List<VideoDTO.VideoDetailResponse>> getMyVideos(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(videoService.getCandidateVideos(userDetails.getUsername()));
    }

    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> streamVideo(@PathVariable Long videoId) throws IOException {
        Path filePath = videoService.getVideoFilePath(videoId);
        Resource resource = new FileSystemResource(filePath);
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        String contentType = Files.probeContentType(filePath);
        if (contentType == null) contentType = "video/mp4";
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PutMapping("/{videoId}/review")
    @PreAuthorize("hasAnyRole('RECRUITER', 'ADMIN')")
    public ResponseEntity<VideoDTO.VideoDetailResponse> reviewVideo(
            @PathVariable Long videoId,
            @RequestBody VideoDTO.VideoReviewRequest request) {
        return ResponseEntity.ok(videoService.reviewVideo(videoId, request));
    }
}
