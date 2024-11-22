package com.esm.faceitstats.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapAverageStats extends UserAverageStats{
    @JsonProperty("map")
    private String map;

    @JsonProperty("times_played")
    private int amount;


    public MapAverageStats(
            String map,
            double kd,
            int aces,
            double kr,
            int quads,
            int triples,
            int doubleKills,
            int mvps,
            int deaths,
            int kills,
            int assists,
            int headshotPercentage,
            double hltvRating,
            double averageDamage
            ) {
        super(kd, kr, quads, triples, doubleKills, mvps, deaths, kills, assists, headshotPercentage, hltvRating, averageDamage, aces);
        this.map = map;
    }
}