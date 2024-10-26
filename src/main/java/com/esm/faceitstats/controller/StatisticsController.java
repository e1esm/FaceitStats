package com.esm.faceitstats.controller;


import com.esm.faceitstats.dto.GetStatsParams;
import com.esm.faceitstats.service.FaceitService;
import com.esm.faceitstats.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class StatisticsController {

    private FaceitService faceitService;

    @Autowired
    public void setFaceitService(FaceitService faceitService) {
        this.faceitService = faceitService;
    }

    @GetMapping("/stats/{id}")
    public ResponseEntity<?> getStatsOf(@PathVariable String id, @ModelAttribute GetStatsParams params) {
        var stats = this.faceitService.getStatsOfUserBy(id, params);
        return ResponseEntity.ok(stats);
    }
}
