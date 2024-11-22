package com.esm.faceitstats.controller;

import com.esm.faceitstats.dto.GetStatsParams;
import com.esm.faceitstats.dto.Match;
import com.esm.faceitstats.service.FaceitService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatisticsController {
    public static final String ALL_MATCHES_PARAMETER = "all_matches";
    public static final String HLTV_RATING_PARAMETER = "hltv_rating";

    private FaceitService faceitService;

    @Autowired
    public void setFaceitService(FaceitService faceitService) {
        this.faceitService = faceitService;
    }

    @GetMapping("/stats/average/{id}")
    public ResponseEntity<?> getAverageStatsByUserID(@PathVariable String id, @RequestParam Map<String, String> requestParams) {
        GetStatsParams params = new GetStatsParams.GetsStatsBuilder().
                withAllMatchesRequired(Boolean.parseBoolean(requestParams.get(StatisticsController.ALL_MATCHES_PARAMETER))).
                withHLTVRequired(Boolean.parseBoolean(requestParams.get(StatisticsController.HLTV_RATING_PARAMETER))).
                build();

        var averageStat = this.faceitService.getAverageStatsOfUserBy(id, params);
        return ResponseEntity.ok(averageStat);
    }

    @GetMapping ("/stats/matches/{id}")
    ResponseEntity<?> getPreviousMatchesByUsersID(@PathVariable String id, @RequestParam Map<String, String> requestParams) {
        GetStatsParams params = new GetStatsParams.GetsStatsBuilder().
                withAllMatchesRequired(Boolean.parseBoolean(requestParams.get(StatisticsController.ALL_MATCHES_PARAMETER))).
                withHLTVRequired(Boolean.parseBoolean(requestParams.get(StatisticsController.HLTV_RATING_PARAMETER))).
                build();

        ArrayList<Match> matches = this.faceitService.getMatchesOfUserByID(id, params);
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/stats/maps/{id}")
    ResponseEntity<?> getStatsByEachMapOfUserByID(@PathVariable String id, @RequestParam Map<String, String> requestParams) {
        GetStatsParams params = new GetStatsParams.GetsStatsBuilder().
                withAllMatchesRequired(Boolean.parseBoolean(requestParams.get(StatisticsController.ALL_MATCHES_PARAMETER))).
                withHLTVRequired(Boolean.parseBoolean(requestParams.get(StatisticsController.HLTV_RATING_PARAMETER))).
                build();

        var statsByMaps = this.faceitService.getAverageStatsByMap(id, params);

        return ResponseEntity.ok(statsByMaps);
    }
}
