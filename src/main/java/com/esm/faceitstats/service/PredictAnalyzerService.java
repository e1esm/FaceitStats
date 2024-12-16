package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.*;
import com.esm.faceitstats.entity.PredictedMatch;
import com.esm.faceitstats.entity.User;
import com.esm.faceitstats.repository.PredictedMatchRepository;
import com.esm.faceitstats.utils.AnalysisComparator;
import com.esm.faceitstats.utils.AnalysisMapComparator;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class PredictAnalyzerService {
    private static final Logger log = LoggerFactory.getLogger(PredictAnalyzerService.class);

    private static final String MATCH_LOBBY = "https://open.faceit.com/data/v4/matches/%s";
    private IHttpRequestBuilder httpClient;
    private PredictedMatchRepository predictedMatchRepository;
    private AnalysisReportExporterService analysisReportExporterService;

    @Autowired
    public void setAnalysisReportExporterService(AnalysisReportExporterService analysisReportExporterService) {
        this.analysisReportExporterService = analysisReportExporterService;
    }

    @Autowired
    public void setPredictedMatchRepository(PredictedMatchRepository predictedMatchRepository) {
        this.predictedMatchRepository = predictedMatchRepository;
    }

    @Autowired
    public void setHttpClient(IHttpRequestBuilder httpClient) {
        this.httpClient = httpClient;
    }

    public List<PredictedMatch> getPredictedMatchesOfUser(Long userID) {
        var tasks =  this.predictedMatchRepository.findMatchesByUserId(userID);
        tasks.sort(Comparator.comparing(PredictedMatch::getCreatedAt, Comparator.reverseOrder()));
        return tasks;
    }

    public List<PredictedMatch> getPredictedMatches() {
        var tasks =  this.predictedMatchRepository.findAll();
        tasks.sort(Comparator.comparing(PredictedMatch::getCreatedAt, Comparator.reverseOrder()));
        return tasks;
    }

    public void doMonthlyAnalyzes(){
        var startDate = Instant.now().minus(Duration.ofDays(30));
        var endDate = Instant.now();

        var allPredictions = this.predictedMatchRepository.findPredictedMatchesByCreatedAtBetween(startDate, endDate);


       var userReliability = this.getPlayersReliability(allPredictions);
       var mapsReliability = this.getMapsReliability(allPredictions);


       this.analysisReportExporterService.save(startDate.toString(), endDate.toString(), mapsReliability, userReliability);
    }


    @Async
    public void analyzePredictedMatch(PredictionResponse predicts, String lobbyID, User user){
        Instant stopTime = Instant.now().plus(24, ChronoUnit.HOURS);
        boolean isFinished = false;

        var matchBuilder = PredictedMatch.builder()
                .user(user)
                .createdAt(Instant.now())
                .matchLink(String.format("https://www.faceit.com/en/cs2/room/%s", lobbyID));


        try {
            while (stopTime.isAfter(Instant.now())) {
                var lobby = this.performRequest(lobbyID);
                if(lobby.getStatus().equalsIgnoreCase("FINISHED")){
                    matchBuilder.foundResultAt(Instant.now());
                    matchBuilder.wonFaction(lobby.getResult().getWinner());
                    matchBuilder.playedMap(lobby.getFinalVote().getMap().getVotedMaps()[0]);
                    matchBuilder.wasPredictionRight(this.checkWhetherPredictionWasCorrect(predicts, lobby));

                    isFinished = true;
                    this.predictedMatchRepository.save(matchBuilder.build());
                    break;
                }

                Long minutesToWait = 5L;
                Thread.sleep(Duration.of(minutesToWait, ChronoUnit.MINUTES).toMillis());
            }
        }catch (InterruptedException e){
            log.error(e.getMessage());
        }

        if(!isFinished){
            matchBuilder.failureMessage("Timeout exceeded");
            this.predictedMatchRepository.save(matchBuilder.build());
        }
    }

    private boolean checkWhetherPredictionWasCorrect(PredictionResponse predicts, LobbyResponse lobby){
        double firstWinProbability = 0;
        for(PredictionResponse.MapExpectation expectation: predicts.getFirstFactionBestMaps()){
            if(expectation.getMap().equalsIgnoreCase(lobby.getFinalVote().getMap().getVotedMaps()[0])){
                firstWinProbability = expectation.getWinPossibility();
                break;
            }
        }

        for(PredictionResponse.MapExpectation expectation: predicts.getSecondFactionBestMaps()){
            if(expectation.getMap().equalsIgnoreCase(lobby.getFinalVote().getMap().getVotedMaps()[0])){
                if(firstWinProbability > expectation.getWinPossibility() && lobby.getResult().getWinner().equalsIgnoreCase(Faction.FIRST_FACTION.getFaction())){
                    return true;
                }

                if(firstWinProbability < expectation.getWinPossibility() && lobby.getResult().getWinner().equalsIgnoreCase(Faction.SECOND_FACTION.getFaction())){
                    return true;
                }

                break;
            }
        }

        return false;
    }

    private AnalysisMapCategories getMapsReliability(List<PredictedMatch> predictedMatches) {
        Map<String, AnalysisMap> mapsReliability = new HashMap<>();

        for(PredictedMatch predictedMatch: predictedMatches){
            mapsReliability.merge(predictedMatch.getPlayedMap(), AnalysisMap
                    .builder()
                            .map(predictedMatch.getPlayedMap())
                            .overallPlayed(1L)
                            .overallPredictionFailed((long) (predictedMatch.isWasPredictionRight() ? 0 : 1))
                    .build(), PredictAnalyzerService::combineMapStats);
        }

        List<AnalysisMap> mapReliability = new ArrayList<>();
        mapsReliability.entrySet().stream().sorted(Map.Entry.comparingByValue(new AnalysisMapComparator())).forEach(user ->
                mapReliability.add(user.getValue())
        );

        return AnalysisMapCategories.builder()
                .mostReliableMaps(mapReliability.subList(mapReliability.size() / 3, mapReliability.size()))
                .leastReliableMaps(mapReliability.subList(0, mapReliability.size() / 3))
                .build();
    }

    private AnalysisUserCategories getPlayersReliability(List<PredictedMatch> predictedMatches){
        Map<String, AnalysisUser> userStatsAccordingToPredictions = new HashMap<>();

        for(PredictedMatch predictedMatch: predictedMatches){
            String link = predictedMatch.getMatchLink();
            var lobby = this.performRequest(link.substring(link.lastIndexOf("/") + 1));

            if(!lobby.getStatus().equalsIgnoreCase("FINISHED")){
                continue;
            }

            var firstTeam = lobby.getTeams().getFirstFaction();
            enrichCurrAnalysisResults(firstTeam, lobby, userStatsAccordingToPredictions);
            var secondTeam = lobby.getTeams().getSecondFaction();
            enrichCurrAnalysisResults(secondTeam, lobby, userStatsAccordingToPredictions);

        }

        List<AnalysisUser> playerReliability = new ArrayList<>();
        userStatsAccordingToPredictions.entrySet().stream().sorted(Map.Entry.comparingByValue(new AnalysisComparator())).forEach(user ->
                playerReliability.add(user.getValue())
        );

        return AnalysisUserCategories
                .builder()
                .leastReliablePlayers(playerReliability.subList(0, playerReliability.size()/ 3))
                .mostReliablePlayers(playerReliability.subList(playerReliability.size() - (playerReliability.size() / 3), playerReliability.size()))
                .build();
    }

    private void enrichCurrAnalysisResults(LobbyResponse.Faction team, LobbyResponse lobby, Map<String, AnalysisUser> userStatsAccordingToPredictions){
        for(LobbyResponse.User user: team.getUsers()){
            boolean didPredictionWork = lobby.getResult().getWinner().equalsIgnoreCase(Faction.FIRST_FACTION.getFaction());
                userStatsAccordingToPredictions.merge(user.getId(), AnalysisUser
                        .builder()
                        .id(user.getId())
                        .username(user.getNickname())
                        .overallMatchesPlayed(1L)
                        .overallPredictionsFailed((long) (didPredictionWork ? 0: 1))
                        .build(), PredictAnalyzerService::combineStats);
        }
    }

    private LobbyResponse performRequest(String id){

        var resp = this.httpClient.getHttpResponse(
                this.httpClient.buildRequestURI(MATCH_LOBBY, id).toString(),
                HttpMethod.GET.name(), HttpRequest.BodyPublishers.noBody());

        LobbyResponse response;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            response = objectMapper.readValue(resp, LobbyResponse.class);
        }catch (JsonProcessingException e) {
            throw new RuntimeException("failed to map json to statistics response class: " + e.getMessage());
        }

        return response;
    }


    private static AnalysisUser combineStats(AnalysisUser first, AnalysisUser second){
        if(first == null){
            return second;
        }else if(second == null){
            return first;
        }else {
            return AnalysisUser.builder()
                    .id(first.getId())
                    .username(first.getUsername())
                    .overallMatchesPlayed(first.getOverallMatchesPlayed() + second.getOverallPredictionsFailed())
                    .overallPredictionsFailed(first.getOverallPredictionsFailed() + second.getOverallMatchesPlayed())
                    .build();
        }
    }

    private static AnalysisMap combineMapStats(AnalysisMap first, AnalysisMap second){
        if(first == null){
            return second;
        }else if(second == null){
            return first;
        }else{
            return AnalysisMap
                    .builder()
                    .overallPredictionFailed(first.getOverallPredictionFailed() + second.getOverallPredictionFailed())
                    .overallPlayed(first.getOverallPlayed() + second.getOverallPlayed())
                    .map(first.getMap())
                    .build();
        }
    }
}
