package com.esm.faceitstats.controller;

import com.esm.faceitstats.dto.*;
import com.esm.faceitstats.service.AuthenticationService;
import com.esm.faceitstats.service.TokenCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private TokenCacheService tokenCacheService;


    @PostMapping("/signup")
    public JwtResponse signUp(@RequestBody SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/signin")
    public JwtResponse signIn(@RequestBody SignInRequest request) {
        SecurityContextHolder.getContext();
        return authenticationService.signIn(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest) {
        this.tokenCacheService.blacklistToken(logoutRequest.getToken(), 86400000);

        return ResponseEntity.ok().build();
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
