package com.interview.repository;

import com.interview.entity.VideoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VideoResponseRepository extends JpaRepository<VideoResponse, Long> {
    List<VideoResponse> findByCandidateIdOrderByUploadedAtDesc(Long candidateId);
    List<VideoResponse> findByStatus(VideoResponse.Status status);

    @Query("SELECT AVG(v.rating) FROM VideoResponse v WHERE v.candidate.id = :candidateId AND v.rating IS NOT NULL")
    Double findAvgRatingByCandidateId(Long candidateId);
}
