package com.esm.faceitstats.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.Authenticator;
import java.net.http.HttpClient;
import java.time.Duration;


@Configuration
public class HttpClientConfig {
    private final int CONNECTION_TIMEOUT = 1;
    private final boolean ARE_REDIRECTS_ENABLED = true;

    @Bean
    public HttpClient httpClient() {
        HttpClient.Redirect redirect;

        redirect = HttpClient.Redirect.NEVER;
        if(this.ARE_REDIRECTS_ENABLED){
            redirect = HttpClient.Redirect.ALWAYS;
        }

        return HttpClient.newBuilder().
                followRedirects(redirect).
                connectTimeout(Duration.ofMinutes(this.CONNECTION_TIMEOUT)).
                build();
    }
}
