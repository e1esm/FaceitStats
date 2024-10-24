package com.esm.faceitstats.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatisticFaceitResponse {
    private Long date;
    private String elo;

    @JsonProperty("i18")
    private String score;

    @JsonProperty("i1")
    private String map;

    @JsonProperty("i6")
    private String kills;

    @JsonProperty("i7")
    private String assists;

    @JsonProperty("i8")
    private String deaths;

    @JsonProperty("c2")
    private String killPerDeathRate;

    @JsonProperty("c3")
    private String killPerRoundRate;

    @JsonProperty("c4")
    private String headShotsRate;

    @JsonProperty("i9")
    private String mvps;

    @JsonProperty("i14")
    private String trippleKills;

    @JsonProperty("i15")
    private String quadroKills;

    @JsonProperty("i16")
    private String aces;
}
