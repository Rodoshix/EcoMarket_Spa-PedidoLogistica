package com.ecomarket.pedidoslogistica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
            .info(new Info()
                .title("API - Pedidos y Logística - EcoMarket SPA")
                .version("1.0")
                .description("Microservicio para la gestión de pedidos y logística."));
    }
}