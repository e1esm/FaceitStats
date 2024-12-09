package com.esm.faceitstats.repository;

import com.esm.faceitstats.entity.PredictedMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PredictedMatchRepository extends JpaRepository<PredictedMatch, Long> {
    @Query("SELECT pm FROM PredictedMatch pm WHERE pm.user.id = :userId")
    List<PredictedMatch> findMatchesByUserId(@Param("userId") Long userId);
    List<PredictedMatch> findPredictedMatchesByCreatedAtBetween(Instant startDate, Instant endDate);
}
