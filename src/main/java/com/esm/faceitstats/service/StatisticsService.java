package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.StatisticFaceitResponse;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class StatisticsService {

    private static final int PAGE_SIZE = 20;
    private static final String GET_STATS_OF_ID = "https://www.faceit.com/api/stats/v1/stats/time/users/%s/games/cs2?size=%d&game_mode=5v5";
    private IHttpRequestBuilder httpClient;

    private ObjectMapper objectMapper;


    @Autowired
    private void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setHttpClient(IHttpRequestBuilder httpClient){
        this.httpClient = httpClient;
    }

    public ArrayList<StatisticFaceitResponse> getStatisticsOfUserID(String ID, boolean areAllMatchesRequired){
        ArrayList<StatisticFaceitResponse> responses = new ArrayList<>();
        boolean toBeProcessed = true;
        String additionalPathVars = "";

        while(toBeProcessed){
            List<StatisticFaceitResponse> stats = this.performRequest(ID, additionalPathVars);
            responses.addAll(stats);

            if(!areAllMatchesRequired || (stats.size() < StatisticsService.PAGE_SIZE)){
                toBeProcessed = false;
                continue;
            }

            additionalPathVars = String.format("&to=%d", stats.getLast().getDate());
        }

        return responses;
    }

    public List<StatisticFaceitResponse> performRequest(String id, String additionalPathVars){
        URI url = this.httpClient.buildRequestURI(
                StatisticsService.GET_STATS_OF_ID + additionalPathVars,
                id,
                Integer.toString(StatisticsService.PAGE_SIZE));

        HttpGet req = new HttpGet(url);
        String content = this.httpClient.getJsonResponse(req);
        StatisticFaceitResponse[] response;

        try {
            response = this.objectMapper.readValue(content, StatisticFaceitResponse[].class);
        }catch (JsonProcessingException e){
            throw new RuntimeException("failed to map json to statistics response class: " + e.getMessage());
        }

        List<StatisticFaceitResponse> stats = new ArrayList<>();
        Collections.addAll(stats, response);

        return stats;
    }

}
