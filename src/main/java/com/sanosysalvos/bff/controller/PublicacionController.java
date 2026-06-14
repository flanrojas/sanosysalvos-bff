package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.client.PublicacionClient;
import com.sanosysalvos.bff.dto.request.PublicacionRequest;
import com.sanosysalvos.bff.dto.response.PublicacionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ms-publicacion")
@Tag(name = "Publicaciones", description = "CRUD para gestionar publicaciones de mascotas")
public class PublicacionController {

    private final PublicacionClient publicacionClient;

    public PublicacionController(PublicacionClient publicacionClient) {
        this.publicacionClient = publicacionClient;
    }

    @GetMapping("/publicaciones")
    @Operation(summary = "Listar publicaciones", description = "Obtiene todas las publicaciones registradas")
    public ResponseEntity<List<PublicacionResponse>> getAll() {
        return ResponseEntity.ok(publicacionClient.getAll());
    }

    @GetMapping("/publicaciones/{id}")
    @Operation(summary = "Obtener publicacion por ID")
    public ResponseEntity<PublicacionResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(publicacionClient.getById(id));
    }

    @PostMapping("/publicaciones")
    @Operation(summary = "Crear publicacion")
    public ResponseEntity<PublicacionResponse> create(@RequestBody PublicacionRequest request) {
        PublicacionResponse response = publicacionClient.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/publicaciones/{id}")
    @Operation(summary = "Actualizar publicacion")
    public ResponseEntity<PublicacionResponse> update(@PathVariable UUID id, @RequestBody PublicacionRequest request) {
        PublicacionResponse response = publicacionClient.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/publicaciones/{id}")
    @Operation(summary = "Eliminar publicacion")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        publicacionClient.delete(id);
        return ResponseEntity.noContent().build();
    }
}