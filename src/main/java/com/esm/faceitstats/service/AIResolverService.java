package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.*;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AIResolverService {
    private static final Logger log = LoggerFactory.getLogger(AIResolverService.class);
    private final String AI_ENDPOINT = "https://free-chatgpt-api.p.rapidapi.com/chat-completion-one?prompt=%s";
    private IHttpRequestBuilder httpClient;

    @Autowired
    public void setHttpClient(IHttpRequestBuilder httpClient) {
        this.httpClient = httpClient;
    }

    public PredictionResponse getPrediction(
            List<MapAverageStats> firstFactionStats,
            List<MapAverageStats> secondFactionStats) {

        String firstStatsJson = getJsonOfStatsList(firstFactionStats);
        String secondStatsJson = getJsonOfStatsList(secondFactionStats);

        AIQuery query = new AIQuery(firstStatsJson, secondStatsJson);
        return getAIPrediction(query.getQuery());

        /*
        ObjectMapper objectMapper = new ObjectMapper();
        try {

        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }
        return null;

         */

    }


    private String getJsonOfStatsList(List<MapAverageStats> list) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<AIQueryStats> queryStatsList = getStatsForAIPrediction(list);
        String json;
        try {
            json = objectMapper.writeValueAsString(queryStatsList);
        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return "";
        }

        return json;
    }


    private List<AIQueryStats> getStatsForAIPrediction(List<MapAverageStats> list) {
        List<AIQueryStats> queryStats = new ArrayList<>();
        for (MapAverageStats mapAverageStats : list) {
            AIQueryStats aiQueryStats = AIQueryStats.builder()
                    .kd(mapAverageStats.getKd())
                    .map(mapAverageStats.getMap())
                    .kr(mapAverageStats.getKr())
                    .wins(mapAverageStats.getWins())
                    .amount(mapAverageStats.getAmount())
                    .build();
            queryStats.add(aiQueryStats);
        }

        return queryStats;
    }

    private PredictionResponse getAIPrediction(String requestBody){
        var uri = this.httpClient.buildRequestURI(AI_ENDPOINT, requestBody);
        var response = this.httpClient.getHttpResponse(
            uri.toString(),
                HttpMethod.GET.name(),
                HttpRequest.BodyPublishers.noBody());


        JSONObject jsonObject = new JSONObject(response);
         var r = jsonObject.getJSONObject("response");

         response = r.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PredictionResponse predictionResponse = objectMapper.readValue(response, PredictionResponse.class);
            return predictionResponse;
        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return null;
    }
}
