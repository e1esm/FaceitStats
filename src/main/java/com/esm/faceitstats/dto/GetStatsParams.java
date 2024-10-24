package com.esm.faceitstats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetStatsParams {
    boolean isHLTVRequired;
    boolean isHistoryOfHLTVRequired;
    boolean isMapRatingRequired;

}
