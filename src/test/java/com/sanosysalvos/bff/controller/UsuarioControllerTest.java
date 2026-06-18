package com.sanosysalvos.bff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.bff.client.UsuarioClient;
import com.sanosysalvos.bff.dto.request.PasswordChangeRequest;
import com.sanosysalvos.bff.dto.response.UserProfileResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UsuarioController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = EnableWebSecurity.class)
)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UsuarioClient usuarioClient;

    @Test
    void obtenerMiPerfil_DebeRetornar200() throws Exception {
        UserProfileResponse perfil = new UserProfileResponse("Juan", "juan@test.com", "Centro", "555", "ROL_DUENO");
        when(usuarioClient.obtenerMiPerfil()).thenReturn(perfil);

        mockMvc.perform(get("/bff/usuarios/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan"))
                .andExpect(jsonPath("$.rol").value("ROL_DUENO"));
    }

    @Test
    void actualizarPerfil_DebeRetornar200() throws Exception {
        UserProfileResponse updateReq = new UserProfileResponse("Juan Editado", "juan@test.com", "Sur", "555", "ROL_DUENO");
        when(usuarioClient.actualizarPerfil(anyString(), any(UserProfileResponse.class))).thenReturn(updateReq);

        mockMvc.perform(put("/bff/usuarios/juan@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Editado"));
    }

    @Test
    void cambiarMiPassword_DebeRetornar200() throws Exception {
        PasswordChangeRequest dto = new PasswordChangeRequest("clave_vieja", "clave_nueva");
        when(usuarioClient.cambiarMiPassword(any(PasswordChangeRequest.class))).thenReturn("Contraseña actualizada exitosamente");

        mockMvc.perform(put("/bff/usuarios/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Contraseña actualizada exitosamente"));
    }

    @Test
    void resetearPasswordAdmin_DebeRetornar200() throws Exception {
        PasswordChangeRequest dto = new PasswordChangeRequest(null, "admin_nueva");
        when(usuarioClient.resetearPasswordAdmin(anyString(), any(PasswordChangeRequest.class))).thenReturn("Contraseña reseteada por Administrador");

        mockMvc.perform(put("/bff/usuarios/juan@test.com/admin-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Contraseña reseteada por Administrador"));
    }

    @Test
    void desactivarMiCuenta_DebeRetornar204() throws Exception {
        doNothing().when(usuarioClient).desactivarMiCuenta();

        mockMvc.perform(delete("/bff/usuarios/me"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarCuentaAdmin_DebeRetornar204() throws Exception {
        doNothing().when(usuarioClient).eliminarCuenta(anyString());

        mockMvc.perform(delete("/bff/usuarios/admin/force-delete/juan@test.com"))
                .andExpect(status().isNoContent());
    }
}