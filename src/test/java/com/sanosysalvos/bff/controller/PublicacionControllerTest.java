package com.sanosysalvos.bff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.bff.client.PublicacionClient;
import com.sanosysalvos.bff.dto.request.PublicacionRequest;
import com.sanosysalvos.bff.dto.response.PublicacionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublicacionController.class)
class PublicacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper =
            new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private PublicacionClient publicacionClient;

    @Test
    void getAll_DebeRetornar200() throws Exception {

        List<PublicacionResponse> publicaciones = List.of(
                new PublicacionResponse(
                        UUID.randomUUID(),
                        "PERDIDA",
                        "Perrito perdido",
                        "Descripción",
                        LocalDateTime.now(),
                        "2026-06-11",
                        "ACTIVA",
                        -33.45,
                        -70.66,
                        "Centro",
                        null,
                        "Juan",
                        "123456789",
                        null,
                        UUID.randomUUID(),
                        UUID.randomUUID()
                )
        );

        when(publicacionClient.getAll())
                .thenReturn(publicaciones);

        mockMvc.perform(get("/ms-publicacion/publicaciones"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].titulo").value("Perrito perdido"))
                .andExpect(jsonPath("$[0].tipoPublicacion").value("PERDIDA"));
    }

    @Test
    void getById_DebeRetornar200() throws Exception {

        UUID id = UUID.randomUUID();

        PublicacionResponse response = new PublicacionResponse(
                id,
                "PERDIDA",
                "Perrito perdido",
                "Descripción",
                LocalDateTime.now(),
                "2026-06-11",
                "ACTIVA",
                -33.45,
                -70.66,
                "Centro",
                null,
                "Juan",
                "123456789",
                null,
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(publicacionClient.getById(id))
                .thenReturn(response);

        mockMvc.perform(get("/ms-publicacion/publicaciones/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idPublicacion").value(id.toString()))
                .andExpect(jsonPath("$.titulo").value("Perrito perdido"))
                .andExpect(jsonPath("$.tipoPublicacion").value("PERDIDA"));
    }

    @Test
    void create_DebeRetornar201() throws Exception {

        UUID mascotaId = UUID.randomUUID();
        UUID usuarioId = UUID.randomUUID();

        PublicacionRequest request = new PublicacionRequest(
                "PERDIDA",
                "Perrito perdido",
                "Descripción",
                "2026-06-11",
                "ACTIVA",
                -33.45,
                -70.66,
                "Centro",
                null,
                "Juan",
                "123456789",
                null,
                mascotaId,
                usuarioId
        );

        PublicacionResponse response = new PublicacionResponse(
                UUID.randomUUID(),
                "PERDIDA",
                "Perrito perdido",
                "Descripción",
                LocalDateTime.now(),
                "2026-06-11",
                "ACTIVA",
                -33.45,
                -70.66,
                "Centro",
                null,
                "Juan",
                "123456789",
                null,
                mascotaId,
                usuarioId
        );

        when(publicacionClient.create(any(PublicacionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/ms-publicacion/publicaciones")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo").value("Perrito perdido"))
                .andExpect(jsonPath("$.tipoPublicacion").value("PERDIDA"));
    }

    @Test
    void update_DebeRetornar200() throws Exception {

        UUID id = UUID.randomUUID();

        PublicacionRequest request = new PublicacionRequest(
                "PERDIDA",
                "Perrito actualizado",
                "Descripción actualizada",
                "2026-06-11",
                "ACTIVA",
                -33.45,
                -70.66,
                "Centro",
                null,
                "Juan",
                "123456789",
                null,
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        PublicacionResponse response = new PublicacionResponse(
                id,
                "PERDIDA",
                "Perrito actualizado",
                "Descripción actualizada",
                LocalDateTime.now(),
                "2026-06-11",
                "ACTIVA",
                -33.45,
                -70.66,
                "Centro",
                null,
                "Juan",
                "123456789",
                null,
                UUID.randomUUID(),
                UUID.randomUUID()
        );

        when(publicacionClient.update(eq(id), any(PublicacionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        put("/ms-publicacion/publicaciones/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPublicacion").value(id.toString()))
                .andExpect(jsonPath("$.titulo").value("Perrito actualizado"));
    }

    @Test
    void delete_DebeRetornar204() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing().when(publicacionClient).delete(id);

        mockMvc.perform(delete("/ms-publicacion/publicaciones/{id}", id))
                .andExpect(status().isNoContent());
    }
}