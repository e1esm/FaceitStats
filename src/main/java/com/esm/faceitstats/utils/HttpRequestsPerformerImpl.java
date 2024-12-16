package com.esm.faceitstats.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Component
public class HttpRequestsPerformerImpl implements IHttpRequestBuilder {
    HttpClient httpClient;

    @Autowired
    public void setHttpClient(HttpClient client){
        this.httpClient = client;
    }

    public URI buildRequestURI(String URL, String... param){
        return URI.create(
                String.format(URL, param));
    }

    public String getHttpResponse(String URL, String methodName, HttpRequest.BodyPublisher body) {
        String response;
        try {
            HttpRequest request = HttpRequest.newBuilder().
                    uri(URI.create(URL)).
                    header("Content-Type", "application/json").
                    header("x-rapidapi-key", "d1a666fb89mshc026fcf47eaa65fp17c8b1jsn8794d3a097e7").
                    header("x-rapidapi-host", "free-chatgpt-api.p.rapidapi.com").
                    header("Authorization", String.format("Bearer %s", System.getenv("auth_token"))).
                    method(methodName, body).
                    build();

            HttpResponse<String> resp = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(resp.statusCode() != HttpStatus.OK.value()){
                throw HttpClientErrorException.create(
                        HttpStatusCode.valueOf(resp.statusCode()),
                        "",
                        null,
                        null,
                        null);
            }
            response = resp.body();
        }catch (IOException | InterruptedException | RuntimeException e){
            if(e.getMessage().contains("502")){
                throw HttpClientErrorException.create(
                        HttpStatusCode.valueOf(502),
                        "Bad Gateway",
                        null, null, null
                );
            }
            throw new RuntimeException(String.format("failed to convert response to string: %s", e));
        }

        return response;
    }
}
