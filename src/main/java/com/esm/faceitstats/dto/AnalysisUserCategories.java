package com.esm.faceitstats.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalysisUserCategories {
    List<AnalysisUser> mostReliablePlayers;
    List<AnalysisUser> leastReliablePlayers;
}
