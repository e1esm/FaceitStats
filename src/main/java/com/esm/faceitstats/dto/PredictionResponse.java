package com.esm.faceitstats.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictionResponse {

    @JsonProperty("first_faction_best_maps")
    MapExpectation[] firstFactionBestMaps;

    @JsonProperty("second_faction_best_maps")
    MapExpectation[] secondFactionBestMaps;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MapExpectation{
        @JsonProperty("map")
        String map;

        @JsonProperty("win_possibility")
        Double winPossibility;
    }
}
