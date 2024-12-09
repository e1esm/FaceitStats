package com.esm.faceitstats.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AnalysisUser {
    private String username;
    private String id;
    private Long overallMatchesPlayed;
    private Long overallPredictionsFailed;
}

