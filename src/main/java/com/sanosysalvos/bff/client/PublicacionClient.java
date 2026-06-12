package com.sanosysalvos.bff.client;

import com.sanosysalvos.bff.dto.request.PublicacionRequest;
import com.sanosysalvos.bff.dto.response.PublicacionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Component
public class PublicacionClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public PublicacionClient(RestTemplate restTemplate, @Value("${ms.publicacion.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<PublicacionResponse> getAll() {
        return restTemplate.exchange(
                baseUrl + "/publicaciones",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PublicacionResponse>>() {}
        ).getBody();
    }

    public PublicacionResponse getById(UUID id) {
        return restTemplate.getForObject(baseUrl + "/publicaciones/" + id, PublicacionResponse.class);
    }

    public PublicacionResponse create(PublicacionRequest request) {
        return restTemplate.postForObject(baseUrl + "/publicaciones", request, PublicacionResponse.class);
    }

    public PublicacionResponse update(UUID id, PublicacionRequest request) {
        return restTemplate.exchange(
                baseUrl + "/publicaciones/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                PublicacionResponse.class
        ).getBody();
    }

    public void delete(UUID id) {
        restTemplate.delete(baseUrl + "/publicaciones/" + id);
    }
}