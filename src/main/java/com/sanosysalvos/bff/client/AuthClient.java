package com.sanosysalvos.bff.client;
import com.sanosysalvos.bff.dto.request.LoginRequest;
import com.sanosysalvos.bff.dto.request.RegistroRequest;
import com.sanosysalvos.bff.dto.response.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AuthClient {

    private final RestClient restClient;

    public AuthClient(
            RestClient restClientGlobal,
            @Value("${ms.auth.base-url}") String baseUrl) {

        this.restClient = restClientGlobal.mutate()
                .baseUrl(baseUrl)
                .build();
    }

    public TokenResponse ingresar(LoginRequest request) {
        return restClient.post()
                .uri("/api/v1/auth/ingreso")
                .body(request)
                .retrieve()
                .body(TokenResponse.class);
    }

    public Object registrar(RegistroRequest request) {
        return restClient.post()
                .uri("/api/v1/auth/registro")
                .body(request)
                .retrieve()
                .body(Object.class);
    }
}