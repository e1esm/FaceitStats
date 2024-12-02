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

    public String getHttpResponse(String URL, String methodName) {
        String response;
        try {
            HttpRequest request = HttpRequest.newBuilder().
                    uri(URI.create(URL)).
                    header("Authorization", String.format("Bearer %s", System.getenv("auth_token"))).
                    method(methodName, HttpRequest.BodyPublishers.noBody()).
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
            throw new RuntimeException(String.format("failed to convert response to string: %s", e));
        }

        return response;
    }
}
