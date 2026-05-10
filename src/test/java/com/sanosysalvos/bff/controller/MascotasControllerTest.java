package com.sanosysalvos.bff.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sanosysalvos.bff.service.MascotasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MascotasControllerTest {

    private static final String PET_ID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String OWNER_ID = "2df6d4ba-ef5e-4ad3-a148-ecb48ff8f933";

    private MascotasService mascotasService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mascotasService = org.mockito.Mockito.mock(MascotasService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new MascotasController(mascotasService)).build();
    }

    @Test
    void getAllDelegatesWithValidFilters() throws Exception {
        when(mascotasService.getAll(OWNER_ID, "LOST"))
                .thenReturn(ResponseEntity.ok("[{\"id\":\"%s\"}]".formatted(PET_ID)));

        mockMvc.perform(get("/ms-mascotas/pets")
                        .param("ownerId", OWNER_ID)
                        .param("status", "LOST"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":\"%s\"}]".formatted(PET_ID)));

        verify(mascotasService).getAll(OWNER_ID, "LOST");
    }

    @Test
    void getAllRejectsInvalidOwnerId() throws Exception {
        mockMvc.perform(get("/ms-mascotas/pets").param("ownerId", "invalid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("ownerId debe ser un UUID valido")));

        verifyNoInteractions(mascotasService);
    }

    @Test
    void getAllRejectsInvalidStatus() throws Exception {
        mockMvc.perform(get("/ms-mascotas/pets").param("status", "ADOPTED"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("status debe ser uno de")));

        verifyNoInteractions(mascotasService);
    }

    @Test
    void getByIdRejectsInvalidId() throws Exception {
        mockMvc.perform(get("/ms-mascotas/pets/invalid-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("id debe ser un UUID valido")));

        verifyNoInteractions(mascotasService);
    }

    @Test
    void createDelegatesWhenBodyIsValid() throws Exception {
        when(mascotasService.create(anyMap()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("{\"id\":\"%s\"}".formatted(PET_ID)));

        mockMvc.perform(post("/ms-mascotas/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Firulais",
                                  "status": "LOST",
                                  "species": "Perro",
                                  "color": "Cafe",
                                  "size": 15.5,
                                  "ownerId": "%s"
                                }
                                """.formatted(OWNER_ID)))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":\"%s\"}".formatted(PET_ID)));

        verify(mascotasService).create(anyMap());
    }

    @Test
    void createRejectsMissingStatus() throws Exception {
        mockMvc.perform(post("/ms-mascotas/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Firulais",
                                  "species": "Perro",
                                  "color": "Cafe",
                                  "size": 15.5
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("status debe ser uno de")));

        verify(mascotasService, never()).create(anyMap());
    }

    @Test
    void updateRejectsEmptyBody() throws Exception {
        mockMvc.perform(put("/ms-mascotas/pets/{id}", PET_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("body no puede estar vacio")));

        verify(mascotasService, never()).update(eq(PET_ID), anyMap());
    }

    @Test
    void patchAllowsPartialBodyWithoutStatus() throws Exception {
        when(mascotasService.patch(eq(PET_ID), anyMap()))
                .thenReturn(ResponseEntity.ok("{\"id\":\"%s\",\"foundLocation\":\"Plaza de Armas\"}".formatted(PET_ID)));

        mockMvc.perform(patch("/ms-mascotas/pets/{id}", PET_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"foundLocation\":\"Plaza de Armas\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"foundLocation\":\"Plaza de Armas\"}"));

        verify(mascotasService).patch(eq(PET_ID), anyMap());
    }

    @Test
    void deleteDelegatesValidId() throws Exception {
        when(mascotasService.delete(PET_ID)).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/ms-mascotas/pets/{id}", PET_ID))
                .andExpect(status().isNoContent());

        verify(mascotasService).delete(PET_ID);
    }
}