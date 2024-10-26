package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.Match;
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
    private static final String GET_STATS_OF_ID = "https://open.faceit.com/data/v4/players/%s/games/cs2/stats";
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

    public ArrayList<Match> getStatisticsOfUserID(String ID, boolean areAllMatchesRequired){
        ArrayList<Match> responses = new ArrayList<>();
        boolean toBeProcessed = true;
        int offset = 0;

        try {
            while (toBeProcessed) {

                List<Match> stats = this.performRequest(ID, offset, StatisticsService.PAGE_SIZE);
                responses.addAll(stats);

                if (!areAllMatchesRequired || (stats.size() < StatisticsService.PAGE_SIZE)) {
                    toBeProcessed = false;
                    continue;
                }

                offset += StatisticsService.PAGE_SIZE;
            }
        }catch (Exception e){
            throw new RuntimeException(String.format("failed to get statistics of user: %s: %s", ID, e.getMessage()));
        }

        return responses;
    }

    public List<Match> performRequest(String id, int from, int to){
        String pagingQueryParams = String.format("?offset=%d&limit=%d", from, to);
        URI url = this.httpClient.buildRequestURI(
                    StatisticsService.GET_STATS_OF_ID + pagingQueryParams,
                    id,
                    Integer.toString(StatisticsService.PAGE_SIZE));

        HttpGet req = new HttpGet(url);
        var resp = this.httpClient.getHttpResponse(req);

        StatisticFaceitResponse response;
        try {
            response = this.objectMapper.readValue(resp, StatisticFaceitResponse.class);
        }catch (JsonProcessingException e){
            throw new RuntimeException("failed to map json to statistics response class: " + e.getMessage());
        }

        List<Match> stats = new ArrayList<>();
        Collections.addAll(stats, response.getMatches());

        return stats;
    }

}
