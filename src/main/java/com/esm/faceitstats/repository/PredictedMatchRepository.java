package com.esm.faceitstats.repository;

import com.esm.faceitstats.entity.PredictedMatch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictedMatchRepository extends JpaRepository<PredictedMatch, Long> {

}
