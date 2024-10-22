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
        URI uri = URI.create(
                String.format(UsernameResolverService.GET_ID_BY_NICKNAME_ENDPOINT,
                        nickname));
        HttpGet httpGet = new HttpGet(uri);


        CloseableHttpResponse resp;
        String response;
        try {
            resp = this.httpClient.execute(httpGet);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        try {
          response = InputStreamConverter.convertStreamToString(resp.getEntity().getContent());
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        UserFaceitResponse payload = null;
        try {
            payload = this.objectMapper.readValue(response, UserFaceitResponse.class);
        }catch(JsonProcessingException e){
            throw new RuntimeException("failed to parse response: " + e.getMessage());
        }

        return payload;
    }

}
