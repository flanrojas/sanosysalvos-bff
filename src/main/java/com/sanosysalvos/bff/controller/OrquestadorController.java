package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.dto.request.ReporteCompletoRequest;
import com.sanosysalvos.bff.dto.response.PublicacionDetalladaResponse;
import com.sanosysalvos.bff.dto.response.ReporteCompletoResponse;
import com.sanosysalvos.bff.service.OrquestadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bff/orquestador")
@Tag(name = "Orquestador", description = "Endpoints compuestos que integran múltiples microservicios (API Composition)")
public class OrquestadorController {

    private final OrquestadorService orquestadorService;

    public OrquestadorController(OrquestadorService orquestadorService) {
        this.orquestadorService = orquestadorService;
    }

    @PostMapping("/publicaciones/completo")
    @Operation(summary = "Crear reporte completo", description = "Crea la mascota y su publicación asociada en una sola petición coordinada")
    public ResponseEntity<ReporteCompletoResponse> crearReporteCompleto(@RequestBody ReporteCompletoRequest request) {
        ReporteCompletoResponse respuesta = orquestadorService.crearReporteCompleto(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @GetMapping("/publicaciones/{id}/detalle")
    @Operation(summary = "Obtener detalle completo de publicación")
    public ResponseEntity<PublicacionDetalladaResponse> getPublicacionDetallada(@PathVariable UUID id) {
        PublicacionDetalladaResponse respuesta = orquestadorService.getPublicacionDetallada(id);
        return ResponseEntity.ok(respuesta);
    }
}