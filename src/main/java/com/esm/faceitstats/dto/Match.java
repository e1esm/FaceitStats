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

    @JsonProperty("Match Id")
    private String matchId;

    @JsonProperty("was_won")
    private boolean win;

    @JsonProperty("stats")
    UserStat userStat;

}