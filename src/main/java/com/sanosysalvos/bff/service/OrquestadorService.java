package com.sanosysalvos.bff.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.sanosysalvos.bff.dto.ReporteCompletoDTO;
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
public class OrquestadorService extends BaseBffService {

    private static final String PUBLICACIONES_PATH = "/publicaciones";
    private static final String PETS_PATH = "/api/v1/pets";

    private final String publicacionBaseUrl;
    private final String mascotasBaseUrl;

    public OrquestadorService(RestTemplate restTemplate,
                              @Value("${ms.publicacion.base-url}") String publicacionBaseUrl,
                              @Value("${ms.mascotas.base-url}") String mascotasBaseUrl) {
        super(restTemplate);
        this.publicacionBaseUrl = publicacionBaseUrl;
        this.mascotasBaseUrl = mascotasBaseUrl;
    }

    public ResponseEntity<Map<String, Object>> getPublicacionDetallada(String idPublicacion) {
        ParameterizedTypeReference<Map<String, Object>> responseType = new ParameterizedTypeReference<>() {};

        try {
            ResponseEntity<Map<String, Object>> pubResponse = restTemplate.exchange(
                    publicacionBaseUrl + PUBLICACIONES_PATH + "/" + idPublicacion,
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

    public Map<String, Object> crearReporteCompleto(ReporteCompletoDTO dto) {
        // 1. Crear Mascota
        Map<String, Object> mascotaReq = new HashMap<>();
        mascotaReq.put("name", dto.nombre);
        mascotaReq.put("species", dto.tipo);
        mascotaReq.put("color", dto.color);
        mascotaReq.put("size", dto.tamaño);
        mascotaReq.put("status", "perdido".equalsIgnoreCase(dto.estado) ? "LOST" : "FOUND");
        mascotaReq.put("ownerId", dto.usuarioId);

        // AQUÍ ESTABA EL BUG: Agregamos mascotasBaseUrl para que la URI sea absoluta
        ResponseEntity<Map> mascotaRes = restTemplate.postForEntity(mascotasBaseUrl + PETS_PATH, mascotaReq, Map.class);
        String mascotaIdGenerado = (String) mascotaRes.getBody().get("id");

        // 2. Crear Publicación
        Map<String, Object> pubReq = new HashMap<>();
        pubReq.put("tipoPublicacion", "perdido".equalsIgnoreCase(dto.estado) ? "PERDIDA" : "ENCONTRADA");
        pubReq.put("titulo", dto.titulo);
        pubReq.put("descripcion", dto.descripcion);
        pubReq.put("mascotaId", mascotaIdGenerado);
        pubReq.put("usuarioId", dto.usuarioId);
        pubReq.put("estado", "ACTIVA");
        pubReq.put("direccionReferencia", dto.ubicacion);
        pubReq.put("latitud", dto.latitud != null ? dto.latitud : -33.68);
        pubReq.put("longitud", dto.longitud != null ? dto.longitud : -71.21);
        pubReq.put("fechaPublicacion", LocalDateTime.now().toString());
        pubReq.put("fechaExtravioOEncuentro", dto.fecha);
        pubReq.put("nombreContacto", dto.nombreContacto);
        pubReq.put("telefonoContacto", dto.telefonoContacto);

        ResponseEntity<Map> pubRes = restTemplate.postForEntity(publicacionBaseUrl + PUBLICACIONES_PATH, pubReq, Map.class);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("mensaje", "Reporte creado y orquestado con éxito");
        resultado.put("publicacion", pubRes.getBody());
        resultado.put("mascota", mascotaRes.getBody());

        return resultado;
    }
}