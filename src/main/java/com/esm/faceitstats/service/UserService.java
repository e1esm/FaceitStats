package com.esm.faceitstats.service;
import com.esm.faceitstats.dto.HttpResponse;
import com.esm.faceitstats.dto.UserFaceitResponse;
import com.esm.faceitstats.exception.ResourceNotFoundException;
import com.esm.faceitstats.utils.IHttpRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class UserService {
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
        HttpGet req;
        try {
            req = new HttpGet(this.httpClient.buildRequestURI(UserService.GET_ID_BY_NICKNAME_ENDPOINT, nickname));
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(String.format("failed to create HTTP request: %s", e.getMessage()));
        }

        HttpResponse response = this.httpClient.getHttpResponse(req);
        if(response.httpCode() != HttpStatus.OK.value()){
            throw HttpClientErrorException.create(
                    HttpStatusCode.valueOf(response.httpCode()),
                    "failed to perform request to the downstream service",
                    null,
                    null,
                    null
                    );
        }

        UserFaceitResponse resp;
        try {
            resp = this.objectMapper.readValue(response.content(), UserFaceitResponse.class);
        }catch(JsonProcessingException e){
            throw new RuntimeException("failed to parse response: " + e.getMessage());
        }

        if(resp.getUserResponse().length == 0){
            throw new ResourceNotFoundException("requested resource was not found");
        }

        return resp;
    }

}
