package com.sanosysalvos.bff.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MascotasService extends BaseBffService {

    private static final String PETS_PATH = "/api/v1/pets";
    private final String baseUrl;

    public MascotasService(RestTemplate restTemplate,
                           @Value("${ms.mascotas.base-url}") String baseUrl) {
        super(restTemplate);
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

        return exchangeString(uriBuilder.toUriString(), HttpMethod.GET, new HttpEntity<>(headers()));
    }

    public ResponseEntity<String> getById(String id) {
        return exchangeString(baseUrl + PETS_PATH + "/" + id, HttpMethod.GET, new HttpEntity<>(headers()));
    }

    public ResponseEntity<String> create(Map<String, Object> request) {
        return exchangeString(baseUrl + PETS_PATH, HttpMethod.POST, new HttpEntity<>(request, headers()));
    }

    public ResponseEntity<String> update(String id, Map<String, Object> request) {
        return exchangeString(baseUrl + PETS_PATH + "/" + id, HttpMethod.PUT, new HttpEntity<>(request, headers()));
    }

    public ResponseEntity<String> patch(String id, Map<String, Object> request) {
        return exchangeString(baseUrl + PETS_PATH + "/" + id, HttpMethod.PATCH, new HttpEntity<>(request, headers()));
    }

    public ResponseEntity<String> delete(String id) {
        return exchangeString(baseUrl + PETS_PATH + "/" + id, HttpMethod.DELETE, new HttpEntity<>(headers()));
    }
}