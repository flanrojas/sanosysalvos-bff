package com.sanosysalvos.bff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.bff.client.MascotasClient;
import com.sanosysalvos.bff.dto.request.MascotaRequest;
import com.sanosysalvos.bff.dto.response.MascotaResponse;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MascotasController.class)
class MascotasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private MascotasClient mascotasClient;

    @Test
    void getAll_DebeRetornar200() throws Exception {

        List<MascotaResponse> mascotas = List.of(
                new MascotaResponse(
                        UUID.randomUUID(),
                        "Fido",
                        "LOST",
                        "Perro",
                        "Negro",
                        10.0,
                        null,
                        "Centro",
                        "Descripción",
                        UUID.randomUUID(),
                        LocalDateTime.now()
                )
        );

        when(mascotasClient.getAll(null, null))
                .thenReturn(mascotas);

        mockMvc.perform(get("/ms-mascotas/pets"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Fido"))
                .andExpect(jsonPath("$[0].species").value("Perro"));
    }

    @Test
    void getById_DebeRetornar200() throws Exception {

        UUID id = UUID.randomUUID();

        MascotaResponse mascota = new MascotaResponse(
                id,
                "Fido",
                "LOST",
                "Perro",
                "Negro",
                10.0,
                null,
                "Centro",
                "Descripción",
                UUID.randomUUID(),
                LocalDateTime.now()
        );

        when(mascotasClient.getById(id))
                .thenReturn(mascota);

        mockMvc.perform(get("/ms-mascotas/pets/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Fido"));
    }

    @Test
    void create_DebeRetornar201() throws Exception {

        MascotaRequest request = new MascotaRequest(
                "Fido",
                "LOST",
                "Perro",
                "Negro",
                10.0,
                null,
                "Centro",
                "Descripción",
                UUID.randomUUID()
        );

        MascotaResponse response = new MascotaResponse(
                UUID.randomUUID(),
                "Fido",
                "LOST",
                "Perro",
                "Negro",
                10.0,
                null,
                "Centro",
                "Descripción",
                UUID.randomUUID(),
                LocalDateTime.now()
        );

        when(mascotasClient.create(any(MascotaRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/ms-mascotas/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Fido"))
                .andExpect(jsonPath("$.status").value("LOST"));
    }

    @Test
    void delete_DebeRetornar204() throws Exception {

        UUID id = UUID.randomUUID();

        doNothing().when(mascotasClient).delete(id);

        mockMvc.perform(delete("/ms-mascotas/pets/{id}", id))
                .andExpect(status().isNoContent());
    }
}