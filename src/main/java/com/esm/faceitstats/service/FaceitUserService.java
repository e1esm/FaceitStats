package com.esm.faceitstats.service;
import com.esm.faceitstats.dto.UserFaceitResponse;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class FaceitUserService {
    private static final String GET_ID_BY_NICKNAME_ENDPOINT = "https://open.faceit.com/data/v4/search/players?nickname=%s";
    private IHttpRequestBuilder httpClient;
    private ObjectMapper objectMapper;


    @Autowired
    private void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setHttpClient(IHttpRequestBuilder httpClient) {
        this.httpClient = httpClient;
    }

    public UserFaceitResponse getUserByUsername(String nickname) {
        String response = this.httpClient.getHttpResponse(this.httpClient.buildRequestURI(FaceitUserService.GET_ID_BY_NICKNAME_ENDPOINT, nickname).toString(), HttpMethod.GET.name());
        UserFaceitResponse resp;

        try {
            resp = this.objectMapper.readValue(response, UserFaceitResponse.class);
        }catch(JsonProcessingException e){
            throw new RuntimeException("failed to parse response: " + e.getMessage());
        }

        return resp;
    }

}
