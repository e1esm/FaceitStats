package com.esm.faceitstats.utils;

import java.net.URI;
import java.net.http.HttpRequest;

public interface IHttpRequestBuilder {
     URI buildRequestURI(String URL, String... param);
     String getHttpResponse(String URL, String methodName, HttpRequest.BodyPublisher body);
}
