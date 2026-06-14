package com.sanosysalvos.bff.service;

import com.sanosysalvos.bff.client.MascotasClient;
import com.sanosysalvos.bff.client.PublicacionClient;
import com.sanosysalvos.bff.dto.request.MascotaRequest;
import com.sanosysalvos.bff.dto.request.PublicacionRequest;
import com.sanosysalvos.bff.dto.request.ReporteCompletoRequest;
import com.sanosysalvos.bff.dto.response.MascotaResponse;
import com.sanosysalvos.bff.dto.response.PublicacionResponse;
import com.sanosysalvos.bff.dto.response.ReporteCompletoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrquestadorServiceTest {

    @Mock
    private MascotasClient mascotasClient;

    @Mock
    private PublicacionClient publicacionClient;

    @InjectMocks
    private OrquestadorService orquestadorService;

    private ReporteCompletoRequest requestMock;
    private MascotaResponse mascotaResponseMock;
    private PublicacionResponse publicacionResponseMock;

    private final UUID usuarioId = UUID.randomUUID();
    private final UUID mascotaId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        requestMock = new ReporteCompletoRequest(
                "Perrito perdido", "Firulais", "Perro", "Café", 15.0,
                "perdido", "Plaza Italia", "2026-06-11", "Tiene collar azul",
                "Juan Pérez", "12345678", usuarioId.toString(), -33.4, -70.6
        );

        mascotaResponseMock = new MascotaResponse(
                mascotaId, "Firulais", "LOST", "Perro", "Café", 15.0,
                null, null, null, usuarioId, LocalDateTime.now()
        );

        publicacionResponseMock = new PublicacionResponse(
                UUID.randomUUID(), "PERDIDA", "Perrito perdido", "Tiene collar azul",
                LocalDateTime.now(), "2026-06-11", "ACTIVA", -33.4, -70.6,
                "Plaza Italia", null, "Juan Pérez", "12345678", null,
                mascotaId, usuarioId
        );
    }

    @Test
    void crearReporteCompleto_Exito() {
        when(mascotasClient.create(any(MascotaRequest.class))).thenReturn(mascotaResponseMock);
        when(publicacionClient.create(any(PublicacionRequest.class))).thenReturn(publicacionResponseMock);

        ReporteCompletoResponse resultado = orquestadorService.crearReporteCompleto(requestMock);

        assertNotNull(resultado);
        assertEquals("Reporte creado y orquestado con éxito", resultado.mensaje());
        assertEquals(mascotaId, resultado.mascota().id());
        assertEquals("PERDIDA", resultado.publicacion().tipoPublicacion());

        verify(mascotasClient, times(1)).create(any(MascotaRequest.class));
        verify(publicacionClient, times(1)).create(any(PublicacionRequest.class));
    }
}