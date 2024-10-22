package com.esm.faceitstats.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserFaceitResponse {

    @JsonProperty("payload")
    private UserResponse userResponse;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserResponse {
        private String id;
        private String nickname;
        private String country;
        private String avatar;

        @JsonProperty("created_at")
        private LocalDateTime createdAt;

        @JsonProperty("games")
        private GameResponse gameResponse;


        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GameResponse {
            @JsonProperty("cs2")
            private GameEntity gameEntity;

        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GameEntity{
            @JsonProperty("faceit_elo")
            private int faceitElo;

            @JsonProperty("skill_level")
            private int skillLevel;
        }

    }

}

