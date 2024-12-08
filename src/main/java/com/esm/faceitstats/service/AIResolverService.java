package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.AIQuery;
import com.esm.faceitstats.dto.MapAverageStats;
import com.esm.faceitstats.dto.PredictionResponse;
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
import java.util.List;

@Service
public class AIResolverService {
    private static final Logger log = LoggerFactory.getLogger(AIResolverService.class);
    private final String AI_ENDPOINT = "https://chatgpt-openai1.p.rapidapi.com/ask";
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

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String q = objectMapper.writeValueAsString(query);
            return getAIPrediction(q);
        }catch (JsonProcessingException e){
            log.error(e.getMessage());
        }
        return null;
    }


    private String getJsonOfStatsList(List<MapAverageStats> list) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json;
        try {
            json = objectMapper.writeValueAsString(list);
        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return "";
        }

        return json;
    }


    private PredictionResponse getAIPrediction(String requestBody){
        var response = this.httpClient.getHttpResponse(
            this.AI_ENDPOINT,
                HttpMethod.POST.name(),
                HttpRequest.BodyPublishers.ofString(requestBody)
        );


        JSONObject jsonObject = new JSONObject(response);
        response = jsonObject.getString("response");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            PredictionResponse resp = objectMapper.readValue(response, PredictionResponse.class);

            return resp;
        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }

        return null;
    }
}
