package com.esm.faceitstats.controller;

import com.esm.faceitstats.dto.UserUpdateRequest;
import com.esm.faceitstats.entity.Role;
import com.esm.faceitstats.entity.User;
import com.esm.faceitstats.service.PlatformUserService;
import com.esm.faceitstats.service.PredictAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {


    private PredictAnalyzerService predictAnalyzerService;
    private PlatformUserService platformUserService;

    @Autowired
    public void setPlatformUserService(PlatformUserService platformUserService) {
        this.platformUserService = platformUserService;
    }

    @Autowired
    public void setPredictAnalyzerService(PredictAnalyzerService predictAnalyzerService) {
        this.predictAnalyzerService = predictAnalyzerService;
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<?> getMatchHistory(@PathVariable Long id) {
        var tasks = this.predictAnalyzerService.getPredictedMatchesOfUser(id);

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/matches/")
    public ResponseEntity<?> getMatchHistory() {
        var tasks = this.predictAnalyzerService.getPredictedMatches();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String name) {
        var uesr = this.platformUserService.getUsers(name);
        return ResponseEntity.ok(uesr);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        var uesr = this.platformUserService.getUser(id);
        return ResponseEntity.ok(uesr);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        this.platformUserService.delete(id);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody() UserUpdateRequest updateRequest) {
        this.platformUserService.updateUser(id, User
                .builder()
                .username(updateRequest.getUsername())
                .faceitLink(updateRequest.getFaceitLink())
                .role(Role.valueOf(updateRequest.getRole()))
                .build());
        return ResponseEntity.ok().build();
    }
}
