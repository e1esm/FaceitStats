package com.esm.faceitstats.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FaceitResponseError {
    String message;
    @JsonProperty("http_status")
    String httpStatus;
}
