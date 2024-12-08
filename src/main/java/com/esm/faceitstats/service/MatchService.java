package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.Match;
import com.esm.faceitstats.utils.AdditionalStatsCalculator;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.concurrent.CompletableFuture;

@Service
public class MatchService {
    private static final String ENRICH_MATCH_STATS = "https://open.faceit.com/data/v4/matches/%s/stats";

    private IHttpRequestBuilder httpClient;

    @Autowired
    public void setHttpClient(IHttpRequestBuilder httpClient) {
        this.httpClient = httpClient;
    }

    @Async
    public CompletableFuture<Void> getAdditionalStatsOfMatch(Match match, String userID, boolean isHLTVRequired) {
        try {
            setMatchADRFromJson(match, userID, performADRRequest(match));
        }catch (JSONException e){
            // approximate calculations of adr because it's still required for hltv rating
            match.getUserStat().setAverageDamage((double) (match.getUserStat().getKills() * 100 / AdditionalStatsCalculator.getOverallRounds(match.getUserStat())));
        }finally {
            if(isHLTVRequired){
                match.getUserStat().setHltvRating(AdditionalStatsCalculator.calculateHLTVRating(match.getUserStat()));
            }
        }
        return null;
    }

    private String performADRRequest(Match match) {
        String response;
        try{
            response = this.httpClient.getHttpResponse(this.httpClient.buildRequestURI(MatchService.ENRICH_MATCH_STATS, match.getMatchId()).toString(), HttpMethod.GET.name(), HttpRequest.BodyPublishers.noBody());
        }catch ( RuntimeException e){
            throw new RuntimeException(String.format("%s: failed to get ADR of a match: %s", match.getMatchId(), e.getMessage()));
        }

        return response;
    }

    private void setMatchADRFromJson(Match match, String userID, String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONObject matchJson = jsonObject.getJSONArray("rounds").getJSONObject(0);
        JSONArray teams = matchJson.getJSONArray("teams");

        for(int i = 0; i < teams.length(); i++){
            JSONObject team = teams.getJSONObject(i);
            JSONArray players = team.getJSONArray("players");

            for(int j = 0; j < players.length(); j++){

                if(players.getJSONObject(j).getString("player_id").equalsIgnoreCase(userID)){
                    JSONObject stats = players.getJSONObject(j).getJSONObject("player_stats");
                    match.getUserStat().setAverageDamage((stats.getDouble("ADR")));
                }
            }
        }
    }

}
