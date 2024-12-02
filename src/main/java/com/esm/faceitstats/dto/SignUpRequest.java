package com.esm.faceitstats.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpRequest {
    private String username;
    private String faceitLink;
    private String password;
}
