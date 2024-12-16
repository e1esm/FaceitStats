package com.esm.faceitstats.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PredictionResponse {

    @JsonProperty("first_faction_best_maps")
    public List<MapExpectation> firstFactionBestMaps;

    @JsonProperty("second_faction_best_maps")
    public List<MapExpectation> secondFactionBestMaps;

    public PredictionResponse() {
        this.firstFactionBestMaps = new ArrayList<>();
        this.secondFactionBestMaps = new ArrayList<>();
    }

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
