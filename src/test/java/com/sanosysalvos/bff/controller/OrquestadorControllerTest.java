package com.sanosysalvos.bff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.bff.dto.request.ReporteCompletoRequest;
import com.sanosysalvos.bff.dto.response.MascotaResponse;
import com.sanosysalvos.bff.dto.response.PublicacionDetalladaResponse;
import com.sanosysalvos.bff.dto.response.PublicacionResponse;
import com.sanosysalvos.bff.dto.response.ReporteCompletoResponse;
import com.sanosysalvos.bff.service.OrquestadorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrquestadorController.class)
class OrquestadorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private OrquestadorService orquestadorService;

    @Test
    void crearReporteCompleto_CuandoRequestValido_DebeRetornar201() throws Exception {

        ReporteCompletoRequest request = new ReporteCompletoRequest(
                "Perrito",
                "Fido",
                "Perro",
                "Negro",
                10.0,
                "perdido",
                "Centro",
                "2026-06-11",
                "Des",
                "Juan",
                "123",
                UUID.randomUUID().toString(),
                0.0,
                0.0
        );

        ReporteCompletoResponse response = new ReporteCompletoResponse(
                "Éxito",
                new MascotaResponse(
                        UUID.randomUUID(),
                        "Fido",
                        "LOST",
                        "Perro",
                        "Negro",
                        10.0,
                        null,
                        null,
                        null,
                        UUID.randomUUID(),
                        LocalDateTime.now()
                ),
                new PublicacionResponse(
                        UUID.randomUUID(),
                        "PERDIDA",
                        "Perrito",
                        "Des",
                        LocalDateTime.now(),
                        "2026",
                        "ACTIVA",
                        0.0,
                        0.0,
                        "Centro",
                        null,
                        "Juan",
                        "123",
                        null,
                        UUID.randomUUID(),
                        UUID.randomUUID()
                )
        );

        when(orquestadorService.crearReporteCompleto(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/bff/orquestador/publicaciones/completo")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensaje").value("Éxito"))
                .andExpect(jsonPath("$.mascota.name").value("Fido"));
    }

    @Test
    void getPublicacionDetallada_CuandoExiste_DebeRetornar200() throws Exception {

        UUID id = UUID.randomUUID();

        PublicacionDetalladaResponse response =
                new PublicacionDetalladaResponse(
                        new PublicacionResponse(
                                id,
                                "PERDIDA",
                                "Perrito",
                                "Descripción",
                                LocalDateTime.now(),
                                "2026",
                                "ACTIVA",
                                -33.45,
                                -70.66,
                                "Centro",
                                null,
                                "Juan",
                                "123",
                                null,
                                UUID.randomUUID(),
                                UUID.randomUUID()
                        ),
                        new MascotaResponse(
                                UUID.randomUUID(),
                                "Fido",
                                "LOST",
                                "Perro",
                                "Negro",
                                10.0,
                                null,
                                null,
                                null,
                                UUID.randomUUID(),
                                LocalDateTime.now()
                        )
                );

        when(orquestadorService.getPublicacionDetallada(id))
                .thenReturn(response);

        mockMvc.perform(
                        get("/bff/orquestador/publicaciones/{id}/detalle", id)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.publicacion.idPublicacion")
                        .value(id.toString()))
                .andExpect(jsonPath("$.mascota.name")
                        .value("Fido"));
    }
}