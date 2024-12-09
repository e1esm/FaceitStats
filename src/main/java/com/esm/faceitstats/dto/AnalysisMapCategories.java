package com.esm.faceitstats.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalysisMapCategories {
    List<AnalysisMap> mostReliableMaps;
    List<AnalysisMap> leastReliableMaps;
}
