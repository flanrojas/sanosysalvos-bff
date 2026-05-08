package com.sanosysalvos.bff.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BffConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    GroupedOpenApi publicacionApi() {
        return GroupedOpenApi.builder()
                .group("ms-publicacion")
                .pathsToMatch("/ms-publicacion/**")
                .build();
    }

    @Bean
    OpenAPI bffOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("BFF MS Publicacion API")
                        .description("BFF REST para consumir el microservicio de publicaciones de mascotas")
                        .version("1.0.0")
                        .contact(new Contact().name("Sanos y Salvos")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("BFF server url"));
    }
}
