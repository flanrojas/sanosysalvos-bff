package com.sanosysalvos.bff.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sanosysalvos.bff.dto.ReporteCompletoDTO;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

class OrquestadorServiceTest {

    private static final String PUBLICACION_ID = "8f937f90-c8f5-4e1c-8be2-2df23b24bd6a";
    private static final String MASCOTA_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String PUBLICACION_BASE_URL = "http://localhost:8080";
    private static final String MASCOTAS_BASE_URL = "http://localhost:8090";

    private RestTemplate restTemplate;
    private OrquestadorService orquestadorService;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        orquestadorService = new OrquestadorService(restTemplate, PUBLICACION_BASE_URL, MASCOTAS_BASE_URL);
    }

    @Test
    void crearReporteCompleto_MakesTwoPostRequestsAndReturnsUnifiedMap() {
        ReporteCompletoDTO dto = new ReporteCompletoDTO();
        dto.nombre = "Luna";
        dto.estado = "perdido";
        dto.usuarioId = "user-123";

        Map<String, Object> mascotaResponse = Map.of("id", MASCOTA_ID);
        Map<String, Object> publicacionResponse = Map.of("idPublicacion", PUBLICACION_ID);

        when(restTemplate.postForEntity(eq(MASCOTAS_BASE_URL + "/api/v1/pets"), any(Map.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(mascotaResponse, HttpStatus.CREATED));

        when(restTemplate.postForEntity(eq(PUBLICACION_BASE_URL + "/publicaciones"), any(Map.class), eq(Map.class)))
                .thenReturn(new ResponseEntity<>(publicacionResponse, HttpStatus.CREATED));

        Map<String, Object> result = orquestadorService.crearReporteCompleto(dto);

        assertNotNull(result);
        assertEquals("Reporte creado y orquestado con éxito", result.get("mensaje"));
        assertEquals(mascotaResponse, result.get("mascota"));
        assertEquals(publicacionResponse, result.get("publicacion"));

        verify(restTemplate).postForEntity(eq(MASCOTAS_BASE_URL + "/api/v1/pets"), any(Map.class), eq(Map.class));
        verify(restTemplate).postForEntity(eq(PUBLICACION_BASE_URL + "/publicaciones"), any(Map.class), eq(Map.class));
    }

    @Test
    void getPublicacionDetallada_MakesTwoGetRequestsAndReturnsUnifiedResponse() {
        Map<String, Object> publicacionMock = Map.of("idPublicacion", PUBLICACION_ID, "mascotaId", MASCOTA_ID);
        Map<String, Object> mascotaMock = Map.of("name", "Luna", "species", "Perro");

        when(restTemplate.exchange(
                eq(PUBLICACION_BASE_URL + "/publicaciones/" + PUBLICACION_ID),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(publicacionMock));

        when(restTemplate.exchange(
                eq(MASCOTAS_BASE_URL + "/api/v1/pets/" + MASCOTA_ID),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(mascotaMock));

        ResponseEntity<Map<String, Object>> response = orquestadorService.getPublicacionDetallada(PUBLICACION_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(publicacionMock, response.getBody().get("publicacion"));
        assertEquals(mascotaMock, response.getBody().get("mascota"));
    }

    @Test
    void getPublicacionDetallada_WhenMascotaServiceFails_ReturnsPublicacionWithErrorInMascota() {
        Map<String, Object> publicacionMock = Map.of("idPublicacion", PUBLICACION_ID, "mascotaId", MASCOTA_ID);

        when(restTemplate.exchange(
                eq(PUBLICACION_BASE_URL + "/publicaciones/" + PUBLICACION_ID),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(publicacionMock));

        when(restTemplate.exchange(
                eq(MASCOTAS_BASE_URL + "/api/v1/pets/" + MASCOTA_ID),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        ResponseEntity<Map<String, Object>> response = orquestadorService.getPublicacionDetallada(PUBLICACION_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        Map<String, Object> mascotaRecibida = (Map<String, Object>) response.getBody().get("mascota");
        assertEquals("Mascota no encontrada o inaccesible", mascotaRecibida.get("error"));
    }
}