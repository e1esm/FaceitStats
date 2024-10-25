package com.esm.faceitstats.utils;


import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

public class ClientHttpCodesMapper {
    public static void ClientCodesResolver(HttpClientErrorException exception){
        if (exception instanceof HttpClientErrorException.BadRequest) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,exception.getResponseBodyAsString(), exception);
        }
        if (exception instanceof HttpClientErrorException.NotFound) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,exception.getResponseBodyAsString(), exception);
        }

        if (exception instanceof HttpClientErrorException.Forbidden) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,exception.getResponseBodyAsString(), exception);
        }
    }
}
