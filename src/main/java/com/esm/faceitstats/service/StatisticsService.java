package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.UserAverageStats;
import com.esm.faceitstats.dto.Match;
import com.esm.faceitstats.dto.StatisticFaceitResponse;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import org.json.*;

@Service
public class StatisticsService {

    private static final int PAGE_SIZE = 20;
    private static final String GET_STATS_OF_ID = "https://open.faceit.com/data/v4/players/%s/games/cs2/stats";
    private static final Logger log = LoggerFactory.getLogger(StatisticsService.class);

    private MatchService matchService;
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

    @Autowired
    public void setMatchService(MatchService matchService){
        this.matchService = matchService;
    }

    public ArrayList<Match> getMatchesOfUserById(String ID, boolean areAllMatchesRequired, boolean isHltvRequired){
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

        this.getAdditionalStatsForMatches(responses, ID, isHltvRequired);

        return responses;
    }

    public UserAverageStats getAverageStatsBasedOnMatches(List<Match> matches){
        UserAverageStats userAverageStats = new UserAverageStats();
        for(Match match : matches){
            userAverageStats.setAdr(userAverageStats.getAdr() + match.getUserStat().getAverageDamage());
            userAverageStats.setKd(userAverageStats.getKd() + match.getUserStat().getKd());
            userAverageStats.setKr(userAverageStats.getKr() + match.getUserStat().getKr());
            userAverageStats.setAces(userAverageStats.getAces() + match.getUserStat().getAces());
            userAverageStats.setAssists(userAverageStats.getAssists() + match.getUserStat().getAssists());
            userAverageStats.setDeaths(userAverageStats.getDeaths() + match.getUserStat().getDeaths());
            userAverageStats.setKills(userAverageStats.getKills() + match.getUserStat().getKills());
            userAverageStats.setMvps(userAverageStats.getMvps() + match.getUserStat().getMvps());
            userAverageStats.setQuadroKills(userAverageStats.getQuadroKills() + match.getUserStat().getQuads());
            userAverageStats.setTrippleKills(userAverageStats.getTrippleKills() + match.getUserStat().getTriples());
            userAverageStats.setDoubleKills(userAverageStats.getDoubleKills() + match.getUserStat().getDoubleKills());
            userAverageStats.setHltvRating(userAverageStats.getHltvRating() + match.getUserStat().getHltvRating());
            userAverageStats.setHeadshotPercentage(userAverageStats.getHeadshotPercentage() + match.getUserStat().getHeadshotPercentage());
        }

        userAverageStats.setAverageOf(matches.size());

        return userAverageStats;
    }


    private void getAdditionalStatsForMatches(ArrayList<Match> matches, String userID, boolean isHLTVRequired){
        CompletableFuture<?>[] futures = new CompletableFuture[matches.size()];

        for(int i = 0; i < matches.size(); i++){
            CompletableFuture<Void> futureResponse = this.matchService.getAdditionalStatsOfMatch(matches.get(i), userID, isHLTVRequired);
            futures[i] = (futureResponse);
        }

        CompletableFuture.allOf(futures).join();
    }

    public List<Match> performRequest(String id, int from, int to){
        String pagingQueryParams = String.format("?offset=%d&limit=%d", from, to);
        String resp = null;
        try {
            resp = this.httpClient.getHttpResponse(
                    this.httpClient.buildRequestURI(
                            StatisticsService.GET_STATS_OF_ID + pagingQueryParams,
                            id,
                            Integer.toString(StatisticsService.PAGE_SIZE)).toString(),
                    HttpMethod.GET.name(), HttpRequest.BodyPublishers.noBody());
        }catch (RuntimeException e){
            log.error(e.getMessage());
            return new ArrayList<>();
        }

        StatisticFaceitResponse response;
        try {

            response = this.objectMapper.readValue(resp, StatisticFaceitResponse.class);
        }catch (JsonProcessingException e){
            throw new RuntimeException("failed to map json to statistics response class: " + e.getMessage());
        }


        List<Match> stats = new ArrayList<>();
        Collections.addAll(stats, response.getMatches());

        mapMatchToID(stats, resp);

        return stats;
    }

    private void mapMatchToID(List<Match> matches, String json){
        JSONObject jsonObject = new JSONObject(json);
        JSONArray arr= jsonObject.getJSONArray("items");
        for(int i=0; i<arr.length(); i++){
            JSONObject obj = arr.getJSONObject(i).getJSONObject("stats");

            matches.get(i).setMatchId(obj.getString("Match Id"));
            matches.get(i).setWin(obj.getInt("Result") == 1);
        }
    }

}
