package com.esm.faceitstats.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFaceitResponse {

    @JsonProperty("items")
    private UserResponse[] userResponse;

    @JsonProperty("errors")
    @JsonIgnore
    private FaceitResponseError[] errors;

}

