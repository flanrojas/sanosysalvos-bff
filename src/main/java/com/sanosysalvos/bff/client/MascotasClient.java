package com.sanosysalvos.bff.client;

import com.sanosysalvos.bff.dto.request.MascotaRequest;
import com.sanosysalvos.bff.dto.response.MascotaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


import java.util.List;
import java.util.UUID;

@Component
public class MascotasClient {

    private final RestClient restClient;

    public MascotasClient(
            RestClient.Builder builder,
            @Value("${ms.mascotas.base-url}") String baseUrl) {

        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
    }

    public List<MascotaResponse> getAll(String ownerId, String status) {

        return restClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder.path("/api/v1/pets");

                    if (ownerId != null) {
                        builder.queryParam("ownerId", ownerId);
                    }

                    if (status != null) {
                        builder.queryParam("status", status);
                    }

                    return builder.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public MascotaResponse getById(UUID id) {
        return restClient.get()
                .uri("/api/v1/pets/{id}", id)
                .retrieve()
                .body(MascotaResponse.class);
    }

    public MascotaResponse create(MascotaRequest request) {
        return restClient.post()
                .uri("/api/v1/pets")
                .body(request)
                .retrieve()
                .body(MascotaResponse.class);
    }

    public void delete(UUID id) {
        restClient.delete()
                .uri("/api/v1/pets/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}