package com.interview.repository;

import com.interview.entity.EvaluationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationResultRepository extends JpaRepository<EvaluationResult, Long> {
    Optional<EvaluationResult> findByCandidateId(Long candidateId);
    List<EvaluationResult> findAllByOrderByOverallScoreDesc();
    List<EvaluationResult> findByStatus(EvaluationResult.Status status);

    @Query("SELECT COUNT(e) FROM EvaluationResult e WHERE e.status = :status")
    Long countByStatus(EvaluationResult.Status status);
}
