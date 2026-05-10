package com.sanosysalvos.bff.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MascotasService {

    private static final String PETS_PATH = "/api/v1/pets";

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public MascotasService(RestTemplate restTemplate,
                           @Value("${ms.mascotas.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public ResponseEntity<String> getAll(String ownerId, String status) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(baseUrl + PETS_PATH);
        if (ownerId != null && !ownerId.isBlank()) {
            uriBuilder.queryParam("ownerId", ownerId);
        }
        if (status != null && !status.isBlank()) {
            uriBuilder.queryParam("status", status);
        }

        return exchange(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers()));
    }

    public ResponseEntity<String> getById(String id) {
        return exchange(baseUrl + PETS_PATH + "/" + id, HttpMethod.GET, new HttpEntity<>(headers()));
    }

    public ResponseEntity<String> create(Map<String, Object> request) {
        return exchange(baseUrl + PETS_PATH, HttpMethod.POST, new HttpEntity<>(request, headers()));
    }

    public ResponseEntity<String> update(String id, Map<String, Object> request) {
        return exchange(baseUrl + PETS_PATH + "/" + id, HttpMethod.PUT, new HttpEntity<>(request, headers()));
    }

    public ResponseEntity<String> patch(String id, Map<String, Object> request) {
        return exchange(baseUrl + PETS_PATH + "/" + id, HttpMethod.PATCH, new HttpEntity<>(request, headers()));
    }

    public ResponseEntity<String> delete(String id) {
        return exchange(baseUrl + PETS_PATH + "/" + id, HttpMethod.DELETE, new HttpEntity<>(headers()));
    }

    private ResponseEntity<String> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity) {
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

    private HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private HttpHeaders responseHeaders(HttpHeaders sourceHeaders) {
        HttpHeaders responseHeaders = new HttpHeaders();
        if (sourceHeaders != null && sourceHeaders.getContentType() != null) {
            responseHeaders.setContentType(sourceHeaders.getContentType());
        } else {
            responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        }
        return responseHeaders;
    }
}
