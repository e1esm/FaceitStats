package com.esm.faceitstats.controller;

import com.esm.faceitstats.dto.UserResponse;
import com.esm.faceitstats.exception.UserNotFoundException;
import com.esm.faceitstats.service.FaceitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

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

            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(response);
    }
}
