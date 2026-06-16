package com.sanosysalvos.bff.controller;

import com.sanosysalvos.bff.client.UsuarioClient;
import com.sanosysalvos.bff.dto.request.PasswordChangeRequest;
import com.sanosysalvos.bff.dto.response.UserProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bff/usuarios")
@Tag(name = "Gestión de Usuarios", description = "Operaciones de perfil de usuario y administración delegadas al MS de Usuarios")
public class UsuarioController {

    private final UsuarioClient usuarioClient;

    public UsuarioController(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios activos (Requiere Rol Admin)")
    public ResponseEntity<Object> listarActivos() {
        return ResponseEntity.ok(usuarioClient.listarActivos());
    }

    @GetMapping("/{username}")
    @Operation(summary = "Obtener usuario por email (Requiere Rol Admin)")
    public ResponseEntity<Object> obtenerPorUsername(@PathVariable String username) {
        return ResponseEntity.ok(usuarioClient.obtenerPorUsername(username));
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener mi perfil (Token del usuario actual)")
    public ResponseEntity<UserProfileResponse> obtenerMiPerfil() {
        return ResponseEntity.ok(usuarioClient.obtenerMiPerfil());
    }

    @PutMapping("/{username}")
    @Operation(summary = "Actualizar perfil (Usuario actual o Admin)")
    public ResponseEntity<UserProfileResponse> actualizarPerfil(
            @PathVariable String username,
            @RequestBody UserProfileResponse updateProfileDTO) {
        return ResponseEntity.ok(usuarioClient.actualizarPerfil(username, updateProfileDTO));
    }

    @PutMapping("/me/password")
    @Operation(summary = "Cambiar mi propia contraseña")
    public ResponseEntity<String> cambiarMiPassword(@RequestBody PasswordChangeRequest passwordDTO) {
        return ResponseEntity.ok(usuarioClient.cambiarMiPassword(passwordDTO));
    }

    @PutMapping("/{username}/admin-password")
    @Operation(summary = "Resetear contraseña de un usuario (Requiere Rol Admin)")
    public ResponseEntity<String> resetearPasswordAdmin(
            @PathVariable String username,
            @RequestBody PasswordChangeRequest passwordDTO) {
        return ResponseEntity.ok(usuarioClient.resetearPasswordAdmin(username, passwordDTO));
    }

    @DeleteMapping("/me")
    @Operation(summary = "Desactivar mi propia cuenta")
    public ResponseEntity<Void> desactivarMiCuenta() {
        usuarioClient.desactivarMiCuenta();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/force-delete/{username}")
    @Operation(summary = "Eliminar usuario permanentemente (Requiere Rol Admin)")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable String username) {
        usuarioClient.eliminarCuenta(username);
        return ResponseEntity.noContent().build();
    }
}