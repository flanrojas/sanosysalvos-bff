package com.sanosysalvos.bff.client;

import com.sanosysalvos.bff.dto.request.PublicacionRequest;
import com.sanosysalvos.bff.dto.response.PublicacionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Component
public class PublicacionClient {

    private final RestClient restClient;

    public PublicacionClient(
            RestClient.Builder builder,
            @Value("${ms.publicacion.base-url}") String baseUrl) {

        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public List<PublicacionResponse> getAll() {
        return restClient.get()
                .uri("/publicaciones")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public PublicacionResponse getById(UUID id) {
        return restClient.get()
                .uri("/publicaciones/{id}", id)
                .retrieve()
                .body(PublicacionResponse.class);
    }

    public PublicacionResponse create(PublicacionRequest request) {
        return restClient.post()
                .uri("/publicaciones")
                .body(request)
                .retrieve()
                .body(PublicacionResponse.class);
    }

    public PublicacionResponse update(UUID id, PublicacionRequest request) {
        return restClient.put()
                .uri("/publicaciones/{id}", id)
                .body(request)
                .retrieve()
                .body(PublicacionResponse.class);
    }

    public void delete(UUID id) {
        restClient.delete()
                .uri("/publicaciones/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}