package com.esm.faceitstats.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateRequest {
    private String username;

    @JsonProperty("faceit_link")
    private String faceitLink;

    private String role;

    @JsonProperty("faceit_link")
    public void setFaceitLink(String faceitLink)
    {
        this.faceitLink = faceitLink;
    }
}
