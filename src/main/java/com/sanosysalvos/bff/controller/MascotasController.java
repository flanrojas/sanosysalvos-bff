package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.client.MascotasClient;
import com.sanosysalvos.bff.dto.request.MascotaRequest;
import com.sanosysalvos.bff.dto.response.MascotaResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ms-mascotas")
@Tag(name = "Mascotas", description = "CRUD para gestionar mascotas desde el BFF")
public class MascotasController {

    private final MascotasClient mascotasClient;

    public MascotasController(MascotasClient mascotasClient) {
        this.mascotasClient = mascotasClient;
    }

    @GetMapping("/pets")
    public ResponseEntity<List<MascotaResponse>> getAll(
            @RequestParam(required = false) String ownerId,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(mascotasClient.getAll(ownerId, status));
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<MascotaResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(mascotasClient.getById(id));
    }

    @PostMapping("/pets")
    public ResponseEntity<MascotaResponse> create(@RequestBody MascotaRequest request) {
        MascotaResponse response = mascotasClient.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/pets/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        mascotasClient.delete(id);
        return ResponseEntity.noContent().build();
    }
}