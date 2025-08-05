package com.tow.mandu.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "OpenAPI Specification - Mandu",
                version = "1.0",
                description = "OpenAPI documentation for Mandu Backend Application",
                contact = @Contact(name = "Mandu Backend Application")
        ),
        security = @SecurityRequirement(name = "bearerAuth"),
        servers = {

  //              @Server(url = "https://towmandu.serveo.net"),
                @Server(url = "http://localhost:8080")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("user-api")
                .packagesToScan("com.tow.mandu.restController")
                .build();
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("all-apis")
                .packagesToScan("com.tow.mandu")
                .build();
    }
}
