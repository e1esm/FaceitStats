package com.esm.faceitstats.utils;

import com.esm.faceitstats.controller.StatisticsController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

public class ClientHttpCodesMapper {
    private static final Logger log = LoggerFactory.getLogger(StatisticsController.class);

    public static ResponseEntity<?> ClientCodesResolver(HttpClientErrorException exception){
        if (exception instanceof HttpClientErrorException.BadRequest) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getResponseBodyAsString());
        }
        if (exception instanceof HttpClientErrorException.NotFound) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getResponseBodyAsString());
        }

        if (exception instanceof HttpClientErrorException.Forbidden) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getResponseBodyAsString());
        }

        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
