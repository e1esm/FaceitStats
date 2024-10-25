package com.esm.faceitstats.controller;

import com.esm.faceitstats.dto.UserResponse;
import com.esm.faceitstats.exception.UserNotFoundException;
import com.esm.faceitstats.service.FaceitService;
import com.esm.faceitstats.utils.ClientHttpCodesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    FaceitService faceitService;

    @Autowired
    void setFaceitService(FaceitService faceitService) {
        this.faceitService = faceitService;
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUserID(@PathVariable String username) {
        UserResponse response;
        try {
            response = this.faceitService.getIDByUsername(username);
        }
        catch(RuntimeException e) {
            if(e instanceof UserNotFoundException){
                throw new UserNotFoundException(String.format("%s: %s", e.getMessage(), username));
            }
            if(e instanceof HttpClientErrorException){
                ClientHttpCodesMapper.ClientCodesResolver((HttpClientErrorException) e);
            }

            log.error(String.format("%s: %s", e.getMessage(), username), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(response);
    }
}
