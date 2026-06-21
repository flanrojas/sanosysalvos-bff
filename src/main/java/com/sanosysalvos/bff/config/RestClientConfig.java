package com.sanosysalvos.bff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient restClientGlobal(JwtForwardingInterceptor jwtInterceptor) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(15000);

        return RestClient.builder()
                .requestFactory(factory)
                .requestInterceptor(jwtInterceptor)
                .defaultStatusHandler(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        (request, response) -> {
                            throw new ResponseStatusException(response.getStatusCode(), "Error en el microservicio");
                        }
                )
                .build();
    }
}