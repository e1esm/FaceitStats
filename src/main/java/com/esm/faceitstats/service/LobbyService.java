package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.LobbyResponse;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LobbyService {
    private static final String MATCH_LOBBY = "https://open.faceit.com/data/v4/matches/%s";

    private IHttpRequestBuilder httpClient;
    private ObjectMapper objectMapper;

    @Autowired
    public void setHttpClient(IHttpRequestBuilder httpClient) {
        this.httpClient = httpClient;
    }

    @Autowired
    private void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public LobbyResponse getLobby(String lobbyLink) {
        String lobbyId = lobbyLink.substring(lobbyLink.lastIndexOf("/") + 1);
        return this.performRequest(lobbyId);
    }

    public LobbyResponse performRequest(String id){

        var resp = this.httpClient.getHttpResponse(
                this.httpClient.buildRequestURI(MATCH_LOBBY, id).toString(),
                HttpMethod.GET.name());

        LobbyResponse response;
        try {
            response = this.objectMapper.readValue(resp, LobbyResponse.class);
        }catch (JsonProcessingException e){
            throw new RuntimeException("failed to map json to statistics response class: " + e.getMessage());
        }



        return response;
    }
}
