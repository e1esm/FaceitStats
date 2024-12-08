package com.esm.faceitstats.controller;

import com.esm.faceitstats.service.PredictAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {


    private PredictAnalyzerService predictAnalyzerService;

    @Autowired
    public void setPredictAnalyzerService(PredictAnalyzerService predictAnalyzerService) {
        this.predictAnalyzerService = predictAnalyzerService;
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<?> getMatchHistory(@PathVariable Long id) {
        var tasks = this.predictAnalyzerService.getPredictedMatch(id);

        return ResponseEntity.ok(tasks);
    }
}
