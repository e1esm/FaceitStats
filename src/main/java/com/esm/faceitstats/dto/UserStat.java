package com.esm.faceitstats.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserStat {
    @JsonProperty("K/D Ratio")
    private double kd;

    @JsonProperty("Penta Kills")
    private int aces;

    @JsonProperty("K/R Ratio")
    private double kr;

    @JsonProperty("Quadro Kills")
    private int quads;

    @JsonProperty("Triple Kills")
    private int triples;

    @JsonProperty("Double Kills")
    private int doubleKills;

    @JsonProperty("MVPs")
    private int mvps;

    @JsonProperty("Score")
    private String score;

    @JsonProperty("Deaths")
    private int deaths;

    @JsonProperty("Kills")
    private int kills;

    @JsonProperty("Assists")
    private int assists;

    @JsonProperty("Headshots %")
    private int headshotPercentage;

    @JsonProperty("Map")
    private String map;

    @JsonProperty("hltv_rating")
    private double hltvRating;

    @JsonProperty("ADR")
    private double averageDamage;
}