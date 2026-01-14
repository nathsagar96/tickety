package com.tickety.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info =
                @Info(
                        title = "Tickety Event Ticket Platform API",
                        version = "1.0.0",
                        description = "RESTful API for event ticket management platform",
                        contact =
                                @Contact(
                                        name = "Tickety Support",
                                        email = "support@tickety.com",
                                        url = "https://tickety.com/support"),
                        license =
                                @License(
                                        name = "MIT License",
                                        url = "https://opensource.org/licenses/MIT")),
        servers = {
            @Server(url = "http://localhost:8080", description = "Local Development"),
            @Server(url = "https://api.tickety.com", description = "Production"),
            @Server(url = "https://staging.tickety.com", description = "Staging")
        },
        security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "JWT authentication token")
public class OpenApiConfig {}
