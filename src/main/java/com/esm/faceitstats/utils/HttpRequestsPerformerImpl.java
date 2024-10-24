package com.esm.faceitstats.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
public class HttpRequestsPerformerImpl implements IHttpRequestBuilder {

    CloseableHttpClient client;

    @Autowired
    public void setClient(CloseableHttpClient client){
        this.client = client;
    }

    public URI buildRequestURI(String URL, String... param){
        return URI.create(
                String.format(URL, param));
    }

    public String getJsonResponse(HttpRequestBase req){
        CloseableHttpResponse resp;
        String response;
        try {
            resp = this.client.execute(req);
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
