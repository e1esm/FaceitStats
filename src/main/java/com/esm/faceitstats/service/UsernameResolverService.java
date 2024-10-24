package com.esm.faceitstats.service;
import com.esm.faceitstats.dto.UserFaceitResponse;
import com.esm.faceitstats.utils.InputStreamConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;

@Service
public class UsernameResolverService {
    private static final String GET_ID_BY_NICKNAME_ENDPOINT = "https://www.faceit.com/api/users/v1/nicknames/%s";
    private CloseableHttpClient httpClient;
    private ObjectMapper objectMapper;


    @Autowired
    private void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public UserFaceitResponse getIdByNickname(String nickname) {
        HttpGet req;
        try {
            req = new HttpGet(buildRequestURI(nickname));
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(String.format("failed to create HTTP request: %s", e.getMessage()));
        }

        String response = getJsonResponse(req);
        UserFaceitResponse payload;

        try {
            payload = this.objectMapper.readValue(response, UserFaceitResponse.class);
        }catch(JsonProcessingException e){
            throw new RuntimeException("failed to parse response: " + e.getMessage());
        }

        return payload;
    }

    private URI buildRequestURI(String param){
        return URI.create(
                String.format(UsernameResolverService.GET_ID_BY_NICKNAME_ENDPOINT,
                        param));
    }

    private String getJsonResponse(HttpGet req){
        CloseableHttpResponse resp;
        String response;
        try {
            resp = this.httpClient.execute(req);
        }catch (IOException e){
            throw new RuntimeException(String.format("failed to execute request: %s", e.getMessage()));
        }

        try {
            response = InputStreamConverter.convertStreamToString(resp.getEntity().getContent());
        }catch (IOException e){
            throw new RuntimeException(String.format("failed to convert response to string: %s", e.getMessage()));
        }

        return response;
    }

}
