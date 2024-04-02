package com.lkathary.kafka.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    private final Environment env;

    @Bean
    public OpenAPI openAPI() {

        String port = env.getProperty("server.port");
        return new OpenAPI()
                .servers(List.of(new Server().url("http://localhost:" + port)))
                .info(new Info().title("Car REST API"));
    }
}
