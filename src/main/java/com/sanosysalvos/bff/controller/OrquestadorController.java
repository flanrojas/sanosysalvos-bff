package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.dto.ReporteCompletoDTO;
import com.sanosysalvos.bff.service.OrquestadorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/bff/orquestador")
public class OrquestadorController {

    private final OrquestadorService orquestadorService;

    public OrquestadorController(OrquestadorService orquestadorService) {
        this.orquestadorService = orquestadorService;
    }

    @PostMapping("/publicaciones/completo")
    public ResponseEntity<?> crearReporteCompleto(@RequestBody ReporteCompletoDTO reporteDTO) {
        try {
            Map<String, Object> respuesta = orquestadorService.crearReporteCompleto(reporteDTO);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al orquestar: " + e.getMessage());
        }
    }

    @GetMapping("/publicaciones/{id}/detalle")
    public ResponseEntity<Map<String, Object>> getPublicacionDetallada(@PathVariable String id) {
        return orquestadorService.getPublicacionDetallada(id);
    }
}