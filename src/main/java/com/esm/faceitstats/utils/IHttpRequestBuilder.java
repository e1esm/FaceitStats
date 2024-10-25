package com.esm.faceitstats.utils;

import com.esm.faceitstats.dto.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import java.net.URI;

public interface IHttpRequestBuilder {
     URI buildRequestURI(String URL, String... param);
     HttpResponse getHttpResponse(HttpRequestBase req);
}
