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
public class JwtResponse {
    @JsonProperty("expiration_date")
    private long expirationDate;

    @JsonProperty("token")
    private String token;
}
