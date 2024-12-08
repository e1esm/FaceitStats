package com.esm.faceitstats.service;


import com.esm.faceitstats.dto.Faction;
import com.esm.faceitstats.dto.LobbyResponse;
import com.esm.faceitstats.dto.PredictionResponse;
import com.esm.faceitstats.entity.PredictedMatch;
import com.esm.faceitstats.entity.User;
import com.esm.faceitstats.repository.PredictedMatchRepository;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@Service
public class PredictAnalyzerService {
    private static final Logger log = LoggerFactory.getLogger(PredictAnalyzerService.class);

    private static final String MATCH_LOBBY = "https://open.faceit.com/data/v4/matches/%s";
    private IHttpRequestBuilder httpClient;
    private PredictedMatchRepository predictedMatchRepository;

    @Autowired
    public void setPredictedMatchRepository(PredictedMatchRepository predictedMatchRepository) {
        this.predictedMatchRepository = predictedMatchRepository;
    }

    @Autowired
    public void setHttpClient(IHttpRequestBuilder httpClient) {
        this.httpClient = httpClient;
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

                Long minutesToWait = 30L;
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
}
