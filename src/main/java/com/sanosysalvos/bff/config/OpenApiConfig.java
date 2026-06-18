package com.sanosysalvos.bff.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    GroupedOpenApi bffApi() {
        return GroupedOpenApi.builder()
                .group("bff-completo")
                .pathsToMatch("/bff/**")
                .build();
    }

    @Bean
    OpenAPI bffOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gateway - Sanos y Salvos")
                        .description("BFF REST para orquestar los microservicios Políglotas (Mascotas, Publicaciones, Usuarios y Geolocalización)")
                        .version("1.0.0")
                        .contact(new Contact().name("Equipo Sanos y Salvos")))
                .addServersItem(new Server()
                        .url("http://localhost:8085")
                        .description("BFF Server Local"));
    }
}
