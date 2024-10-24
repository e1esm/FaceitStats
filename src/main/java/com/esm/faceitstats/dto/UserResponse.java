package com.esm.faceitstats.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    @JsonProperty("player_id")
    private String id;

    private String nickname;
    private String country;
    private String avatar;

    @JsonProperty("games")
    private GameEntity[] gameResponse;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GameEntity{
        @JsonProperty("skill_level")
        private String skillLevel;
        private String name;
    }

}