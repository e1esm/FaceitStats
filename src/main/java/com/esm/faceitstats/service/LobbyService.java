package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.LobbyResponse;
import com.esm.faceitstats.dto.PredictionResponse;
import com.esm.faceitstats.entity.User;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.net.http.HttpRequest;

@Service
public class LobbyService {
    private static final String MATCH_LOBBY = "https://open.faceit.com/data/v4/matches/%s";

    private IHttpRequestBuilder httpClient;
    private FaceitService faceitService;
    private AIResolverService aiResolverService;
    private ObjectMapper objectMapper;
    private PredictAnalyzerService predictAnalyzerService;
    private PlatformUserService platformUserService;

    @Autowired
    public void setPlatformUserService(PlatformUserService platformUserService) {
        this.platformUserService = platformUserService;
    }

    @Autowired
    public void setPredictAnalyzerService(PredictAnalyzerService predictAnalyzerService) {
        this.predictAnalyzerService = predictAnalyzerService;
    }

    @Autowired
    public void setAiResolverService(AIResolverService aiResolverService) {
        this.aiResolverService = aiResolverService;
    }

    @Autowired
    public void setFaceitService(FaceitService faceitService) {
        this.faceitService = faceitService;
    }

    @Autowired
    public void setHttpClient(IHttpRequestBuilder httpClient) {
        this.httpClient = httpClient;
    }

    @Autowired
    private void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public PredictionResponse getLobby(String lobbyLink) {
        String lobbyId = lobbyLink.substring(lobbyLink.lastIndexOf("/") + 1);
        var lobby = this.performRequest(lobbyId);
        User user = this.platformUserService.getCurrentUser();

        var firstTeamAverageStats = this.faceitService.getAverageStatsByFaction(lobby.getTeams().getFirstFaction());
        var secondTeamAverageStats = this.faceitService.getAverageStatsByFaction(lobby.getTeams().getSecondFaction());

        var predicts =  this.aiResolverService.getPrediction(firstTeamAverageStats, secondTeamAverageStats);


        this.predictAnalyzerService.analyzePredictedMatch(predicts, lobbyId, user);

        return predicts;
    }


    private LobbyResponse performRequest(String id){

        var resp = this.httpClient.getHttpResponse(
                this.httpClient.buildRequestURI(MATCH_LOBBY, id).toString(),
                HttpMethod.GET.name(), HttpRequest.BodyPublishers.noBody());

        LobbyResponse response;
        try {
            response = this.objectMapper.readValue(resp, LobbyResponse.class);
        }catch (JsonProcessingException e) {
            throw new RuntimeException("failed to map json to statistics response class: " + e.getMessage());
        }

        return response;
    }
}
