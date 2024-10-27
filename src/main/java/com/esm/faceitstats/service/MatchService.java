package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.Match;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {
    private static final String ENRICH_MATCH_STATS = "https://open.faceit.com/data/v4/matches/%s/stats";

    private IHttpRequestBuilder httpClient;

    @Autowired
    public void setHttpClient(IHttpRequestBuilder httpClient) {
        this.httpClient = httpClient;
    }


    public void getADROfMatchForUser(Match match, String userID) {
        setMatchADRFromJson(match, userID, performADRRequest(match));
    }

    private String performADRRequest(Match match) {
        String response;
        try{
            HttpGet req = new HttpGet(String.format(ENRICH_MATCH_STATS, match.getMatchId()));
            response = this.httpClient.getHttpResponse(req);

        }catch (IllegalArgumentException | JSONException e){
            throw new RuntimeException(String.format("%s: failed to get ADR of a match: %s", match.getMatchId(), e));
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
                    match.getMatchStat().setAverageDamage((stats.getDouble("ADR")));
                }
            }
        }
    }

}
