package com.esm.faceitstats.controller;

import com.esm.faceitstats.dto.*;
import com.esm.faceitstats.entity.User;
import com.esm.faceitstats.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        var user = this.authenticationService.getCurrentUser();
        return ResponseEntity.ok(CurrentUserResponse.builder()
                        .id(user.getId())
                        .role(user.getRole().toString())
                        .faceitLink(user.getFaceitLink())
                        .username(user.getUsername())
                .build());
    }
}
