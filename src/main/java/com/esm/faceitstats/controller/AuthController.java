package com.esm.faceitstats.controller;

import com.esm.faceitstats.dto.JwtResponse;
import com.esm.faceitstats.dto.SignInRequest;
import com.esm.faceitstats.dto.SignUpRequest;
import com.esm.faceitstats.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/signup")
    public JwtResponse signUp(@RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/signin")
    public JwtResponse signIn(@RequestBody SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
