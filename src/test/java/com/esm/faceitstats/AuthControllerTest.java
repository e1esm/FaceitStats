package com.esm.faceitstats;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.esm.faceitstats.entity.Role;
import com.esm.faceitstats.entity.User;
import com.esm.faceitstats.service.AuthenticationService;
import com.esm.faceitstats.dto.JwtResponse;
import com.esm.faceitstats.dto.SignInRequest;
import com.esm.faceitstats.dto.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.Instant;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void testSignUp_Success() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("test@gmail.com", "password123", "some_faceit_link");
        JwtResponse mockJwtResponse = new JwtResponse(System.currentTimeMillis(), "token");

        String signUpRequestJson = objectMapper.writeValueAsString(signUpRequest);


        when(authenticationService.signUp(any(SignUpRequest.class))).thenReturn(mockJwtResponse);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signUpRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").value(mockJwtResponse.getToken()))
                .andExpect(jsonPath("expiration_date").value(mockJwtResponse.getExpirationDate()));

        verify(authenticationService, times(1)).signUp(any(SignUpRequest.class));
    }

    @Test
    public void testSignIn_Success() throws Exception {
        SignInRequest signInRequest = new SignInRequest("testUser", "password123");
        JwtResponse mockJwtResponse = new JwtResponse(System.currentTimeMillis(), "token");
        String signInRequestJson = objectMapper.writeValueAsString(signInRequest);


        when(authenticationService.signIn(any(SignInRequest.class))).thenReturn(mockJwtResponse);

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signInRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("token").value("token"));

        verify(authenticationService, times(1)).signIn(any(SignInRequest.class));

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testMe_Success() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var mockCurrentUser = new User(1L, authentication.getName(), "eeee", "some_faceit_link", Role.ROLE_USER, Instant.now());

        when(authenticationService.getCurrentUser()).thenReturn(mockCurrentUser);

        mockMvc.perform(get("/api/auth/me")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(mockCurrentUser.getId()))
                .andExpect(jsonPath("role").value(mockCurrentUser.getRole().toString()))
                .andExpect(jsonPath("faceit_link").value(mockCurrentUser.getFaceitLink()))
                .andExpect(jsonPath("username").value(authentication.getName()));

        verify(authenticationService, times(1)).getCurrentUser();
    }

}

