package com.sanosysalvos.bff.service;

import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

public abstract class BaseBffService {

    protected final RestTemplate restTemplate;

    protected BaseBffService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    protected ResponseEntity<String> exchangeString(String url, HttpMethod method, HttpEntity<?> requestEntity) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, method, requestEntity, String.class);
            return ResponseEntity.status(response.getStatusCode())
                    .headers(responseHeaders(response.getHeaders()))
                    .body(response.getBody());
        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .headers(responseHeaders(ex.getResponseHeaders()))
                    .body(ex.getResponseBodyAsString());
        }
    }

    protected HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    protected HttpHeaders responseHeaders(HttpHeaders sourceHeaders) {
        HttpHeaders responseHeaders = new HttpHeaders();
        if (sourceHeaders != null && sourceHeaders.getContentType() != null) {
            responseHeaders.setContentType(sourceHeaders.getContentType());
        } else {
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        return responseHeaders;
    }
}