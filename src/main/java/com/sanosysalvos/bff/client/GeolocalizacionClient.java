package com.sanosysalvos.bff.client;

import com.sanosysalvos.bff.dto.request.DistanceRequest;
import com.sanosysalvos.bff.dto.request.RadiusRequest;
import com.sanosysalvos.bff.dto.request.LocationCreateRequest;
import com.sanosysalvos.bff.dto.response.DistanceResponse;
import com.sanosysalvos.bff.dto.response.RadiusResponse;
import com.sanosysalvos.bff.dto.response.LocationResponse;
import com.sanosysalvos.bff.dto.response.MessageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.util.UUID;

@Component
public class GeolocalizacionClient {

    private final RestClient restClient;

    public GeolocalizacionClient(
            RestClient restClientGlobal,
            @Value("${ms.geolocalizacion.base-url}") String baseUrl) {

        this.restClient = restClientGlobal.mutate()
                .baseUrl(baseUrl)
                .build();
    }

    public DistanceResponse calcularDistancia(DistanceRequest request) {
        return restClient.post()
                .uri("/utilities/distance")
                .body(request)
                .retrieve()
                .body(DistanceResponse.class);
    }

    public RadiusResponse buscarMascotasEnRadio(RadiusRequest request) {
        return restClient.post()
                .uri("/utilities/radius")
                .body(request)
                .retrieve()
                .body(RadiusResponse.class);
    }


    public MessageResponse guardarUbicacion(LocationCreateRequest request) {
        return restClient.post()
                .uri("/locations")
                .body(request)
                .retrieve()
                .body(MessageResponse.class);
    }

    public LocationResponse obtenerUbicacion(UUID petId) {
        return restClient.get()
                .uri("/locations/{pet_id}", petId)
                .retrieve()
                .body(LocationResponse.class);
    }

    public void eliminarUbicacion(UUID petId) {
        restClient.delete()
                .uri("/locations/{pet_id}", petId)
                .retrieve()
                .toBodilessEntity();
    }
}