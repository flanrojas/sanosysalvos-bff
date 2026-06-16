package com.sanosysalvos.bff.client;

import com.sanosysalvos.bff.dto.request.PasswordChangeRequest;
import com.sanosysalvos.bff.dto.response.UserProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UsuarioClient {

    private final RestClient restClient;

    public UsuarioClient(
            RestClient restClientGlobal,
            @Value("${ms.auth.base-url}") String baseUrl) {

        this.restClient = restClientGlobal.mutate()
                .baseUrl(baseUrl)
                .build();
    }

    public Object listarActivos() {
        return restClient.get()
                .uri("/api/v1/usuarios")
                .retrieve()
                .body(Object.class);
    }

    public Object obtenerPorUsername(String username) {
        return restClient.get()
                .uri("/api/v1/usuarios/{username}", username)
                .retrieve()
                .body(Object.class);
    }

    public UserProfileResponse obtenerMiPerfil() {
        return restClient.get()
                .uri("/api/v1/usuarios/me")
                .retrieve()
                .body(UserProfileResponse.class);
    }

    public UserProfileResponse actualizarPerfil(String username, UserProfileResponse updateProfileDTO) {
        return restClient.put()
                .uri("/api/v1/usuarios/{username}", username)
                .body(updateProfileDTO)
                .retrieve()
                .body(UserProfileResponse.class);
    }

    public String cambiarMiPassword(PasswordChangeRequest passwordDTO) {
        return restClient.put()
                .uri("/api/v1/usuarios/me/password")
                .body(passwordDTO)
                .retrieve()
                .body(String.class);
    }

    public String resetearPasswordAdmin(String username, PasswordChangeRequest passwordDTO) {
        return restClient.put()
                .uri("/api/v1/usuarios/{username}/admin-password", username)
                .body(passwordDTO)
                .retrieve()
                .body(String.class);
    }

    public void desactivarMiCuenta() {
        restClient.delete()
                .uri("/api/v1/usuarios/me")
                .retrieve()
                .toBodilessEntity();
    }

    public void eliminarCuenta(String username) {
        restClient.delete()
                .uri("/api/v1/usuarios/admin/force-delete/{username}", username)
                .retrieve()
                .toBodilessEntity();
    }
}