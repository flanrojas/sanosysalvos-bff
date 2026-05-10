package com.sanosysalvos.bff.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sanosysalvos.bff.dto.ReporteCompletoDTO;
import com.sanosysalvos.bff.service.OrquestadorService;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class OrquestadorControllerTest {

    private static final String PUBLICACION_ID = "8f937f90-c8f5-4e1c-8be2-2df23b24bd6a";

    private OrquestadorService orquestadorService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        orquestadorService = org.mockito.Mockito.mock(OrquestadorService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new OrquestadorController(orquestadorService)).build();
    }

    @Test
    void crearReporteCompletoDelegatesToService() throws Exception {
        when(orquestadorService.crearReporteCompleto(any(ReporteCompletoDTO.class)))
                .thenReturn(Map.of("mensaje", "Reporte creado y orquestado con éxito"));

        mockMvc.perform(post("/bff/orquestador/publicaciones/completo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Gato perdido",
                                  "nombre": "Michi",
                                  "estado": "perdido"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "mensaje": "Reporte creado y orquestado con éxito"
                        }
                        """));

        verify(orquestadorService).crearReporteCompleto(any(ReporteCompletoDTO.class));
    }

    @Test
    void getPublicacionDetalladaDelegatesToService() throws Exception {
        when(orquestadorService.getPublicacionDetallada(PUBLICACION_ID))
                .thenReturn(ResponseEntity.ok(Map.of(
                        "publicacion", Map.of("idPublicacion", PUBLICACION_ID),
                        "mascota", Map.of("name", "Michi")
                )));

        mockMvc.perform(get("/bff/orquestador/publicaciones/{id}/detalle", PUBLICACION_ID))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "publicacion": {
                            "idPublicacion": "%s"
                          },
                          "mascota": {
                            "name": "Michi"
                          }
                        }
                        """.formatted(PUBLICACION_ID)));

        verify(orquestadorService).getPublicacionDetallada(PUBLICACION_ID);
    }
}