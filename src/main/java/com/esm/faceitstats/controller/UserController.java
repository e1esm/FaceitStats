package com.esm.faceitstats.controller;

import com.esm.faceitstats.dto.UserFaceitResponse;
import com.esm.faceitstats.service.UsernameResolverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    UsernameResolverService usernameResolverService;

    @Autowired
    void setUsernameResolverService(UsernameResolverService usernameResolverService) {
        this.usernameResolverService = usernameResolverService;
    }

    @GetMapping("/users/{username}")
    public ResponseEntity<?> getUserID(@PathVariable String username) {
        UserFaceitResponse response;
        try {
            response = this.usernameResolverService.getIdByNickname(username);
        }catch(RuntimeException e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(response);
    }
}
