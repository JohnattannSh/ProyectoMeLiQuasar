package com.starwars.meli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Configuración de Swagger para la documentación de la API REST.
 * <p>
 * Esta clase define un bean de OpenAPI que contiene la información básica de la API,
 * como el título, versión y descripción, que se mostrará en la interfaz de Swagger UI.
 * </p>
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST Operación Fuego de Quásar / Johnattan Suarez Hoyos")
                        .version("1.0.0")
                        .description("Documentación de la API REST Operación Fuego de Quásar, con sus dos servicios, topsecret y topsecret_split"));
    }
}
