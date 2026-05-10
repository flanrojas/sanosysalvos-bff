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

class PublicacionServiceTest {

    private static final String BASE_URL = "http://localhost:8081";
    private static final String PUBLICACION_ID = "8f937f90-c8f5-4e1c-8be2-2df23b24bd6a";

    private MockRestServiceServer server;
    private PublicacionService service;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        service = new PublicacionService(restTemplate, BASE_URL);
    }

    @Test
    void getAllForwardsGetToPublicacionesEndpoint() {
        server.expect(requestTo(BASE_URL + "/publicaciones"))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE))
                .andRespond(withSuccess("[{\"idPublicacion\":\"" + PUBLICACION_ID + "\"}]", MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = service.getAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains(PUBLICACION_ID);
        server.verify();
    }

    @Test
    void getByIdForwardsGetToPublicacionId() {
        server.expect(requestTo(BASE_URL + "/publicaciones/" + PUBLICACION_ID))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"idPublicacion\":\"" + PUBLICACION_ID + "\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = service.getById(PUBLICACION_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(PUBLICACION_ID);
        server.verify();
    }

    @Test
    void getByIdPreservesNotFoundErrorBody() {
        server.expect(requestTo(BASE_URL + "/publicaciones/" + PUBLICACION_ID))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\":\"Publicacion no encontrada\"}"));

        ResponseEntity<String> response = service.getById(PUBLICACION_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(response.getBody()).contains("Publicacion no encontrada");
        server.verify();
    }

    @Test
    void createForwardsPostBodyAndReturnsCreated() {
        server.expect(requestTo(BASE_URL + "/publicaciones"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {
                          "tipoPublicacion": "PERDIDA",
                          "titulo": "Se busca perro mestizo en Providencia",
                          "estado": "ACTIVA"
                        }
                        """))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"idPublicacion\":\"" + PUBLICACION_ID + "\"}"));

        ResponseEntity<String> response = service.create(Map.of(
                "tipoPublicacion", "PERDIDA",
                "titulo", "Se busca perro mestizo en Providencia",
                "estado", "ACTIVA"
        ));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).contains(PUBLICACION_ID);
        server.verify();
    }

    @Test
    void updateForwardsPutBodyToPublicacionId() {
        server.expect(requestTo(BASE_URL + "/publicaciones/" + PUBLICACION_ID))
                .andExpect(method(HttpMethod.PUT))
                .andExpect(content().json("""
                        {
                          "tipoPublicacion": "ENCONTRADA",
                          "titulo": "Perro encontrado en Providencia",
                          "estado": "ACTIVA"
                        }
                        """))
                .andRespond(withSuccess("{\"tipoPublicacion\":\"ENCONTRADA\"}", MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = service.update(PUBLICACION_ID, Map.of(
                "tipoPublicacion", "ENCONTRADA",
                "titulo", "Perro encontrado en Providencia",
                "estado", "ACTIVA"
        ));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("ENCONTRADA");
        server.verify();
    }

    @Test
    void deleteForwardsDeleteToPublicacionId() {
        server.expect(requestTo(BASE_URL + "/publicaciones/" + PUBLICACION_ID))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK));

        ResponseEntity<String> response = service.delete(PUBLICACION_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        server.verify();
    }

    @Test
    void deletePreservesNotFoundErrorBody() {
        server.expect(requestTo(BASE_URL + "/publicaciones/" + PUBLICACION_ID))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\":\"Publicacion no encontrada\"}"));

        ResponseEntity<String> response = service.delete(PUBLICACION_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Publicacion no encontrada");
        server.verify();
    }
}
