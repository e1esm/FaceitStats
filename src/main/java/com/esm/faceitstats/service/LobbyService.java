package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.LobbyResponse;
import com.esm.faceitstats.dto.PredictionResponse;

import com.esm.faceitstats.entity.PredictedMatch;
import com.esm.faceitstats.entity.User;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.net.http.HttpRequest;
import java.util.*;

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
        predicts = getAdjustedPredictions(predicts.firstFactionBestMaps, predicts.secondFactionBestMaps);

        this.predictAnalyzerService.analyzePredictedMatch(predicts, lobbyId, user);

        predicts.firstFactionBestMaps.sort(Comparator.comparing(PredictionResponse.MapExpectation::getWinPossibility, Comparator.reverseOrder()));
        predicts.secondFactionBestMaps.sort(Comparator.comparing(PredictionResponse.MapExpectation::getWinPossibility, Comparator.reverseOrder()));


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

    private PredictionResponse getAdjustedPredictions(List<PredictionResponse.MapExpectation> firstTeamExpectations, List<PredictionResponse.MapExpectation> secondTeamExpectations) {
        HashMap<String, ArrayList<Double>> winPossibilitiesByMap = new HashMap<>();

        for (PredictionResponse.MapExpectation firstTeamExpectation : firstTeamExpectations) {
            if(!winPossibilitiesByMap.containsKey(firstTeamExpectation.getMap())){
                ArrayList<Double> possibilityList = new ArrayList<>(List.of(0.0, 0.0));
                winPossibilitiesByMap.put(firstTeamExpectation.getMap(), possibilityList);
            }

            var al = winPossibilitiesByMap.get(firstTeamExpectation.getMap());
            al.set(0, firstTeamExpectation.getWinPossibility());
            winPossibilitiesByMap.put(firstTeamExpectation.getMap(), al);
        }


        for(PredictionResponse.MapExpectation secondTeamExpectation : secondTeamExpectations){
            if(!winPossibilitiesByMap.containsKey(secondTeamExpectation.getMap())){
                ArrayList<Double> possibilityList = new ArrayList<>(List.of(0.0, 0.0));
                winPossibilitiesByMap.put(secondTeamExpectation.getMap(), possibilityList);
            }
            var al = winPossibilitiesByMap.get(secondTeamExpectation.getMap());
            al.set(1, secondTeamExpectation.getWinPossibility());
            winPossibilitiesByMap.put(secondTeamExpectation.getMap(), al);
        }

        var response = new PredictionResponse();

        for(Map.Entry<String, ArrayList<Double>> entry: winPossibilitiesByMap.entrySet()){
            var expectationsByTeam = entry.getValue();
            if(expectationsByTeam.get(0) != null && expectationsByTeam.get(1) != null){
                if(expectationsByTeam.get(0) > expectationsByTeam.get(1)){
                    response.firstFactionBestMaps.add(new PredictionResponse.MapExpectation(entry.getKey(), expectationsByTeam.get(0)));
                    response.secondFactionBestMaps.add(new PredictionResponse.MapExpectation(entry.getKey(), 1 - expectationsByTeam.get(0)));
                }else{
                    response.secondFactionBestMaps.add(new PredictionResponse.MapExpectation(entry.getKey(), expectationsByTeam.get(0)));
                    response.firstFactionBestMaps.add(new PredictionResponse.MapExpectation(entry.getKey(), 1 - expectationsByTeam.get(1)));
                }
            }else if(expectationsByTeam.get(0) == null){
                response.secondFactionBestMaps.add(new PredictionResponse.MapExpectation(entry.getKey(), expectationsByTeam.get(1)));
            }else{
                response.firstFactionBestMaps.add(new PredictionResponse.MapExpectation(entry.getKey(), expectationsByTeam.get(0)));
            }
        }

        response.secondFactionBestMaps = getClearedExpectations(response.secondFactionBestMaps);
        response.firstFactionBestMaps = getClearedExpectations(response.firstFactionBestMaps);

        return response;
    }

    private List<PredictionResponse.MapExpectation> getClearedExpectations(List<PredictionResponse.MapExpectation> expectations){
        List<PredictionResponse.MapExpectation> clearedExpectations = new ArrayList<>();
        for(PredictionResponse.MapExpectation expectation: expectations){
            if(!expectation.getMap().contains("de_")){
                continue;
            }
            clearedExpectations.add(expectation);
        }

        return clearedExpectations;
    }
}
