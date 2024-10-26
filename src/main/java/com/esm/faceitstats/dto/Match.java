package com.esm.faceitstats.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Match{

    @JsonProperty("stats")
    MatchStat matchStat;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MatchStat {
        @JsonProperty("K/D Ratio")
        private Double kd;

        @JsonProperty("Penta Kills")
        private Integer aces;

        @JsonProperty("K/R Ratio")
        private Double kr;

        @JsonProperty("Quadro Kills")
        private Integer quads;

        @JsonProperty("Triple Kills")
        private Integer triples;

        @JsonProperty("Double Kills")
        private Integer doubleKills;

        @JsonProperty("MVPs")
        private Integer mvps;

        @JsonProperty("Score")
        private String score;

        @JsonProperty("Deaths")
        private Integer deaths;

        @JsonProperty("Kills")
        private Integer kills;

        @JsonProperty("Assists")
        private Integer assists;

        @JsonProperty("Headshots %")
        private Integer headshotPercentage;

        @JsonProperty("Map")
        private String map;

    }
}