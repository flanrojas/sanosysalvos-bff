package com.sanosysalvos.bff.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class PublicacionService extends BaseBffService {

    private static final String PUBLICACIONES_PATH = "/publicaciones";
    private static final String PETS_PATH = "/api/v1/pets";

    private final String baseUrl;
    private final String mascotasBaseUrl;

    public PublicacionService(RestTemplate restTemplate,
                              @Value("${ms.publicacion.base-url}") String baseUrl,
                              @Value("${ms.mascotas.base-url}") String mascotasBaseUrl) {
        super(restTemplate);
        this.baseUrl = baseUrl;
        this.mascotasBaseUrl = mascotasBaseUrl;
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


    public ResponseEntity<Map<String, Object>> getPublicacionDetallada(String idPublicacion) {
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<Map<String, Object>>() {};

        try {
            ResponseEntity<Map<String, Object>> pubResponse = restTemplate.exchange(
                    baseUrl + PUBLICACIONES_PATH + "/" + idPublicacion,
                    HttpMethod.GET,
                    new HttpEntity<>(headers()),
                    responseType
            );

            Map<String, Object> publicacion = pubResponse.getBody();
            if (publicacion == null) {
                return ResponseEntity.notFound().build();
            }

            String mascotaId = (String) publicacion.get("mascotaId");
            Map<String, Object> mascota = new HashMap<>();

            if (mascotaId != null) {
                try {
                    ResponseEntity<Map<String, Object>> petResponse = restTemplate.exchange(
                            mascotasBaseUrl + PETS_PATH + "/" + mascotaId,
                            HttpMethod.GET,
                            new HttpEntity<>(headers()),
                            responseType
                    );
                    if (petResponse.getBody() != null) {
                        mascota = petResponse.getBody();
                    }
                } catch (HttpStatusCodeException ex) {
                    mascota.put("error", "Mascota no encontrada o inaccesible");
                }
            }

            Map<String, Object> respuestaUnificada = new HashMap<>();
            respuestaUnificada.put("publicacion", publicacion);
            respuestaUnificada.put("mascota", mascota);

            return ResponseEntity.ok(respuestaUnificada);

        } catch (HttpStatusCodeException ex) {
            return ResponseEntity.status(ex.getStatusCode()).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}