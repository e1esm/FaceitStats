package com.esm.faceitstats.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalysisMap {
    private String map;
    private Long overallPlayed;
    private Long overallPredictionFailed;
}
