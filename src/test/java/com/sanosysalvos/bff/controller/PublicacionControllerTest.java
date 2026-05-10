package com.sanosysalvos.bff.controller;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sanosysalvos.bff.service.PublicacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class PublicacionControllerTest {

    private static final String PUBLICACION_ID = "8f937f90-c8f5-4e1c-8be2-2df23b24bd6a";

    private PublicacionService publicacionService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        publicacionService = org.mockito.Mockito.mock(PublicacionService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new PublicacionController(publicacionService)).build();
    }

    @Test
    void getAllDelegatesToService() throws Exception {
        when(publicacionService.getAll())
                .thenReturn(ResponseEntity.ok("[{\"idPublicacion\":\"" + PUBLICACION_ID + "\"}]"));

        mockMvc.perform(get("/ms-publicacion/publicaciones"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"idPublicacion\":\"" + PUBLICACION_ID + "\"}]"));

        verify(publicacionService).getAll();
    }

    @Test
    void getByIdDelegatesToService() throws Exception {
        when(publicacionService.getById(PUBLICACION_ID))
                .thenReturn(ResponseEntity.ok("{\"idPublicacion\":\"" + PUBLICACION_ID + "\"}"));

        mockMvc.perform(get("/ms-publicacion/publicaciones/{id}", PUBLICACION_ID))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"idPublicacion\":\"" + PUBLICACION_ID + "\"}"));

        verify(publicacionService).getById(PUBLICACION_ID);
    }

    @Test
    void createDelegatesBodyToService() throws Exception {
        when(publicacionService.create(anyMap()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED)
                        .body("{\"idPublicacion\":\"" + PUBLICACION_ID + "\"}"));

        mockMvc.perform(post("/ms-publicacion/publicaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "tipoPublicacion": "PERDIDA",
                                  "titulo": "Se busca perro mestizo en Providencia",
                                  "estado": "ACTIVA"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"idPublicacion\":\"" + PUBLICACION_ID + "\"}"));

        verify(publicacionService).create(anyMap());
    }

    @Test
    void updateDelegatesIdAndBodyToService() throws Exception {
        when(publicacionService.update(eq(PUBLICACION_ID), anyMap()))
                .thenReturn(ResponseEntity.ok("{\"idPublicacion\":\"" + PUBLICACION_ID + "\",\"tipoPublicacion\":\"ENCONTRADA\"}"));

        mockMvc.perform(put("/ms-publicacion/publicaciones/{id}", PUBLICACION_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "tipoPublicacion": "ENCONTRADA",
                                  "titulo": "Perro encontrado en Providencia",
                                  "estado": "ACTIVA"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"tipoPublicacion\":\"ENCONTRADA\"}"));

        verify(publicacionService).update(eq(PUBLICACION_ID), anyMap());
    }

    @Test
    void deleteDelegatesToService() throws Exception {
        when(publicacionService.delete(PUBLICACION_ID)).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/ms-publicacion/publicaciones/{id}", PUBLICACION_ID))
                .andExpect(status().isOk());

        verify(publicacionService).delete(PUBLICACION_ID);
    }
}
