package com.esm.faceitstats.controller;

import com.esm.faceitstats.exception.ResourceNotFoundException;
import com.esm.faceitstats.utils.ClientHttpCodesMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;

@RestControllerAdvice
public class ExceptionController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleException(EntityNotFoundException e){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        return new ResponseEntity<>(getJsonOfException(e.getMessage()), headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleException(HttpClientErrorException e){
        var clientHTTPException = ClientHttpCodesMapper.ClientCodesResolver(e);
        if(clientHTTPException.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)){
            log.error(e.getMessage());
        }

        return clientHTTPException;
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<?> handleException(ResourceNotFoundException e) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        return new ResponseEntity<>(getJsonOfException(e.getMessage()), headers, HttpStatus.NOT_FOUND);
    }

    private String getJsonOfException(String param){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error", param);
        jsonObject.put("timestamp", Instant.now());

        return jsonObject.toString();
    }

}

