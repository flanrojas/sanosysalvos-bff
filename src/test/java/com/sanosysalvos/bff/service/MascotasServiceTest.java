package com.sanosysalvos.bff.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

class MascotasServiceTest {

    private static final String BASE_URL = "http://localhost:8090";
    private static final String PET_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String OWNER_ID = "2df6d4ba-ef5e-4ad3-a148-ecb48ff8f933";

    private MockRestServiceServer server;
    private MascotasService service;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        service = new MascotasService(restTemplate, BASE_URL);
    }

    @Test
    void getAllForwardsFiltersToPetsEndpoint() {
        server.expect(requestTo(BASE_URL + "/api/v1/pets?ownerId=" + OWNER_ID + "&status=LOST"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andRespond(withSuccess("[{\"id\":\"" + PET_ID + "\"}]", MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = service.getAll(OWNER_ID, "LOST");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains(PET_ID);
        server.verify();
    }

    @Test
    void getAllPreservesNoContentResponse() {
        server.expect(requestTo(BASE_URL + "/api/v1/pets"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        ResponseEntity<String> response = service.getAll(null, null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
        server.verify();
    }

    @Test
    void getByIdPreservesNotFoundErrorBody() {
        server.expect(requestTo(BASE_URL + "/api/v1/pets/" + PET_ID))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\":\"Mascota no encontrada\"}"));

        ResponseEntity<String> response = service.getById(PET_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains("Mascota no encontrada");
        server.verify();
    }

    @Test
    void createForwardsPostBodyAndReturnsCreated() {
        server.expect(requestTo(BASE_URL + "/api/v1/pets"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {
                          "name": "Firulais",
                          "status": "LOST",
                          "species": "Perro",
                          "color": "Cafe",
                          "size": 15.5,
                          "ownerId": "2df6d4ba-ef5e-4ad3-a148-ecb48ff8f933"
                        }
                        """))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"id\":\"" + PET_ID + "\"}"));

        ResponseEntity<String> response = service.create(Map.of(
                "name", "Firulais",
                "status", "LOST",
                "species", "Perro",
                "color", "Cafe",
                "size", 15.5,
                "ownerId", OWNER_ID
        ));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains(PET_ID);
        server.verify();
    }

    @Test
    void updateForwardsPutToPetId() {
        server.expect(requestTo(BASE_URL + "/api/v1/pets/" + PET_ID))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json("{\"status\":\"AT_HOME\"}"))
                .andRespond(withSuccess("{\"status\":\"AT_HOME\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = service.update(PET_ID, Map.of("status", "AT_HOME"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("AT_HOME");
        server.verify();
    }

    @Test
    void patchForwardsPatchToPetId() {
        server.expect(requestTo(BASE_URL + "/api/v1/pets/" + PET_ID))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(content().json("{\"foundLocation\":\"Plaza de Armas\"}"))
                .andRespond(withSuccess("{\"foundLocation\":\"Plaza de Armas\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = service.patch(PET_ID, Map.of("foundLocation", "Plaza de Armas"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Plaza de Armas");
        server.verify();
    }

    @Test
    void deleteForwardsDeleteToPetId() {
        server.expect(requestTo(BASE_URL + "/api/v1/pets/" + PET_ID))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        ResponseEntity<String> response = service.delete(PET_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        server.verify();
    }
}
