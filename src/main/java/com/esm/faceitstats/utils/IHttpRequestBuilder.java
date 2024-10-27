package com.esm.faceitstats.utils;

import java.net.URI;

public interface IHttpRequestBuilder {
     URI buildRequestURI(String URL, String... param);
     String getHttpResponse(String URL, String methodName);
}
