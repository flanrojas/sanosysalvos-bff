package com.sanosysalvos.bff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.bff.client.AuthClient;
import com.sanosysalvos.bff.dto.request.LoginRequest;
import com.sanosysalvos.bff.dto.request.RegistroRequest;
import com.sanosysalvos.bff.dto.response.TokenResponse;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = EnableWebSecurity.class)
)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthClient authClient;

    @Test
    void ingresar_DebeRetornar200YToken() throws Exception {
        LoginRequest request = new LoginRequest("test@correo.com", "1234");
        TokenResponse response = new TokenResponse("jwt-simulado");

        when(authClient.ingresar(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/bff/auth/ingreso")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-simulado"));
    }

    @Test
    void registrar_DebeRetornar201() throws Exception {
        RegistroRequest request = new RegistroRequest("Pedro", "pass", "pedro@correo.com", "Direccion", "123");

        when(authClient.registrar(any(RegistroRequest.class))).thenReturn("Usuario Registrado");

        mockMvc.perform(post("/bff/auth/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}