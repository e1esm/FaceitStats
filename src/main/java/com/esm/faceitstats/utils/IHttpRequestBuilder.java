package com.esm.faceitstats.utils;

import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;

public interface IHttpRequestBuilder {
    public URI buildRequestURI(String URL, String... param);
    String getJsonResponse(HttpRequestBase req);
}
