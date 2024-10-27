package com.esm.faceitstats.controller;

import com.esm.faceitstats.dto.GetStatsParams;
import com.esm.faceitstats.dto.Match;
import com.esm.faceitstats.exception.ResourceNotFoundException;
import com.esm.faceitstats.service.FaceitService;
import com.esm.faceitstats.utils.ClientHttpCodesMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StatisticsController {
    public static final String ALL_MATCHES_PARAMETER = "all_matches";
    public static final String HLTV_RATING_PARAMETER = "hltv_rating";
    private static final Logger log = LoggerFactory.getLogger(StatisticsController.class);

    private FaceitService faceitService;


    @Autowired
    public void setFaceitService(FaceitService faceitService) {
        this.faceitService = faceitService;
    }

    @GetMapping("/stats/{id}")
    public ResponseEntity<?> getStatsOf(@PathVariable String id, @RequestParam Map<String, String> requestParams) {
        GetStatsParams params = new GetStatsParams.GetsStatsBuilder().
                withAllMatchesRequired(Boolean.parseBoolean(requestParams.get(StatisticsController.ALL_MATCHES_PARAMETER))).
                withHLTVRequired(Boolean.parseBoolean(requestParams.get(StatisticsController.HLTV_RATING_PARAMETER))).
                build();

        ArrayList<Match> stats;
        try {
            stats = (ArrayList<Match>) this.faceitService.getStatsOfUserBy(id, params);
        }catch (RuntimeException e) {
            if(e instanceof EntityNotFoundException) {
                throw new ResourceNotFoundException(String.format("%s: %s", e.getMessage(), id));
            }

            if(e instanceof HttpClientErrorException){
                ClientHttpCodesMapper.ClientCodesResolver((HttpClientErrorException) e);
            }

            log.error(String.format("%s: %s", e.getMessage(), id), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(stats);
    }
}
