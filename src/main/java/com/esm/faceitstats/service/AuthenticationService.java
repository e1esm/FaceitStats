package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.JwtResponse;
import com.esm.faceitstats.dto.SignInRequest;
import com.esm.faceitstats.dto.SignUpRequest;
import com.esm.faceitstats.entity.Role;
import com.esm.faceitstats.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PlatformUserService userService;
    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .faceitLink(request.getFaceitLink())
                .createdAt(Instant.now())
                .build();

        userService.create(user);

        return jwtService.generateToken(user);
    }

    public JwtResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        return jwtService.generateToken(user);
    }
}