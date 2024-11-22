package com.esm.faceitstats.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAverageStats {

    @JsonProperty("kd_rate")
    private double kd;

    @JsonProperty("kr_rate")
    private double kr;

    @JsonProperty("quadro_kills")
    private double quadroKills;

    @JsonProperty("tripple_kills")
    private double trippleKills;

    @JsonProperty("double_kills")
    private double doubleKills;

    @JsonProperty("mvps")
    private double mvps;

    @JsonProperty("deaths")
    private double deaths;

    @JsonProperty("kills")
    private double kills;

    @JsonProperty("assists")
    private double assists;

    @JsonProperty("headshot_percentage")
    private double headshotPercentage;

    @JsonProperty("hltv_rating")
    private double hltvRating;

    @JsonProperty("adr")
    private double adr;

    @JsonProperty("aces")
    private double aces;

    public void setAverageOf(int n){
        this.adr /= n;
        this.hltvRating /= n;
        this.headshotPercentage /= n;
        this.assists /= n;
        this.kills /= n;
        this.deaths /= n;
        this.trippleKills /= n;
        this.doubleKills /= n;
        this.quadroKills /= n;
        this.aces /= n;
        this.mvps /= n;
        this.kd /= n;
        this.kr /= n;
    }
}
