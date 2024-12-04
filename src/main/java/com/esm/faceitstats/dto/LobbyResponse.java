package com.esm.faceitstats.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class LobbyResponse {

    @JsonProperty("match_id")
    private String id;

    @JsonProperty("teams")
    private Teams teams;


    @JsonProperty("voting")
    private Vote finalVote;

    @JsonProperty("started_at")
    private Long startedAt;

    @JsonProperty("results")
    private Result result;

    @JsonProperty("status")
    private String status;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Teams{
        @JsonProperty("faction1")
        private Faction firstFaction;
        @JsonProperty("faction2")
        private Faction secondFaction;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Faction{
        @JsonProperty("roster")
        private User[] users;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User{
        @JsonProperty("player_id")
        private String id;

        @JsonProperty("nickname")
        private String nickname;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Vote{
        private Map map;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Map{
        @JsonProperty("pick")
        private String[] votedMaps;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result{
        @JsonProperty("winner")
        private String winner;
    }
}

