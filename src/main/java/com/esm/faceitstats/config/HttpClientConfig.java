package com.esm.faceitstats.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {
    private static final Logger log = LoggerFactory.getLogger(HttpClientConfig.class);
    private final int CONNECTION_TIMEOUT = 500000;
    private final boolean ARE_REDIRECTS_ENABLED = true;

    @Bean
    public CloseableHttpClient httpClient(@Autowired RequestConfig config) {
        return HttpClientBuilder.
                create().
                setDefaultRequestConfig(config).
                build();
    }


    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.
                custom().
                setConnectTimeout(CONNECTION_TIMEOUT).
                setRedirectsEnabled(ARE_REDIRECTS_ENABLED).
                build();
    }
}
