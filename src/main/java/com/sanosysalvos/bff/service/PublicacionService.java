package com.sanosysalvos.bff.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PublicacionService extends BaseBffService {

    private static final String PUBLICACIONES_PATH = "/publicaciones";
    private final String baseUrl;

    public PublicacionService(RestTemplate restTemplate,
                              @Value("${ms.publicacion.base-url}") String baseUrl) {
        super(restTemplate);
        this.baseUrl = baseUrl;
    }

    public ResponseEntity<String> getAll() {
        return exchangeString(baseUrl + PUBLICACIONES_PATH, HttpMethod.GET, new HttpEntity<>(headers()));
    }

    public ResponseEntity<String> getById(String id) {
        return exchangeString(baseUrl + PUBLICACIONES_PATH + "/" + id, HttpMethod.GET, new HttpEntity<>(headers()));
    }

    public ResponseEntity<String> create(Map<String, Object> request) {
        return exchangeString(baseUrl + PUBLICACIONES_PATH, HttpMethod.POST, new HttpEntity<>(request, headers()));
    }

    public ResponseEntity<String> update(String id, Map<String, Object> request) {
        return exchangeString(baseUrl + PUBLICACIONES_PATH + "/" + id, HttpMethod.PUT, new HttpEntity<>(request, headers()));
    }

    public ResponseEntity<String> delete(String id) {
        return exchangeString(baseUrl + PUBLICACIONES_PATH + "/" + id, HttpMethod.DELETE, new HttpEntity<>(headers()));
    }
}