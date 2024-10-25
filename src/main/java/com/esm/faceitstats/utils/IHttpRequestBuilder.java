package com.esm.faceitstats.utils;

import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;

public interface IHttpRequestBuilder {
     URI buildRequestURI(String URL, String... param);
     String getHttpResponse(HttpRequestBase req);
}
