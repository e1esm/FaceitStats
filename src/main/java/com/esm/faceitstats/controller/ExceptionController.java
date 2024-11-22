package com.esm.faceitstats.controller;

import com.esm.faceitstats.utils.ClientHttpCodesMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ExceptionController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleException(EntityNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleException(HttpClientErrorException e){
        var clientHTTPException = ClientHttpCodesMapper.ClientCodesResolver(e);
        if(clientHTTPException.getStatusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR)){
            log.error(e.getMessage());
        }

        return clientHTTPException;
    }

}

