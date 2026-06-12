package com.sanosysalvos.bff.client;

import com.sanosysalvos.bff.dto.request.MascotaRequest;
import com.sanosysalvos.bff.dto.response.MascotaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Component
public class MascotasClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public MascotasClient(RestTemplate restTemplate, @Value("${ms.mascotas.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<MascotaResponse> getAll(String ownerId, String status) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(baseUrl + "/api/v1/pets");
        if (ownerId != null) uri.queryParam("ownerId", ownerId);
        if (status != null) uri.queryParam("status", status);

        return restTemplate.exchange(
                uri.toUriString(), HttpMethod.GET, null,
                new ParameterizedTypeReference<List<MascotaResponse>>() {}
        ).getBody();
    }

    public MascotaResponse getById(UUID id) {
        return restTemplate.getForObject(baseUrl + "/api/v1/pets/" + id, MascotaResponse.class);
    }

    public MascotaResponse create(MascotaRequest request) {
        return restTemplate.postForObject(baseUrl + "/api/v1/pets", request, MascotaResponse.class);
    }

    public void delete(UUID id) {
        restTemplate.delete(baseUrl + "/api/v1/pets/" + id);
    }
}