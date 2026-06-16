package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.client.AuthClient;
import com.sanosysalvos.bff.dto.request.LoginRequest;
import com.sanosysalvos.bff.dto.request.RegistroRequest;
import com.sanosysalvos.bff.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bff/auth")
@Tag(name = "Autenticación", description = "Endpoints públicos de acceso (No requieren Token)")
public class AuthController {

    private final AuthClient authClient;

    public AuthController(AuthClient authClient) {
        this.authClient = authClient;
    }

    @PostMapping("/ingreso")
    @Operation(summary = "Iniciar sesión para obtener el JWT")
    public ResponseEntity<TokenResponse> ingresar(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authClient.ingresar(request));
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar un nuevo usuario (Rol dueño por defecto)")
    public ResponseEntity<Object> registrar(@RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authClient.registrar(request));
    }
}