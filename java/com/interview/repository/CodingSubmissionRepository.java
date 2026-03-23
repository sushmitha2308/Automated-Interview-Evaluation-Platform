package com.interview.repository;

import com.interview.entity.CodingSubmission;
import com.interview.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CodingSubmissionRepository extends JpaRepository<CodingSubmission, Long> {
    List<CodingSubmission> findByCandidateOrderBySubmittedAtDesc(User candidate);
    List<CodingSubmission> findByCandidateId(Long candidateId);
    Optional<CodingSubmission> findByCandidateIdAndQuestionId(Long candidateId, Long questionId);

    @Query("SELECT AVG(s.score) FROM CodingSubmission s WHERE s.candidate.id = :candidateId")
    Double findAvgScoreByCandidateId(Long candidateId);

    @Query("SELECT COUNT(s) FROM CodingSubmission s WHERE s.candidate.id = :candidateId AND s.status = 'PASSED'")
    Long countPassedByCandidateId(Long candidateId);
}
