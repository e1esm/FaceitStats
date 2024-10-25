package com.esm.faceitstats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;



public record HttpResponse(String content, int httpCode){
    public HttpResponse(String content, int httpCode){
        this.content = content;
        this.httpCode = httpCode;
    }
}
