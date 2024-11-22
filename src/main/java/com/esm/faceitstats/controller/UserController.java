package com.esm.faceitstats.controller;

import com.esm.faceitstats.dto.UserResponse;
import com.esm.faceitstats.service.FaceitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        UserResponse response = this.faceitService.getIDByUsername(username);
        return ResponseEntity.ok(response);
    }
}
