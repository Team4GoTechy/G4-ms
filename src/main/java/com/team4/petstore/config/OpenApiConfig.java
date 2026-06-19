package com.team4.petstore.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String description = "API REST completa para Pet Store. Incluye dos módulos principales:\n" +
            "- **E-Commerce**: Gestión de productos, categorías, carrito de compras, compras y usuarios\n" +
            "- **Gestión Veterinaria (Vet-Stock)**: Gestión de insumos médicos, stock, solicitudes de reposición, órdenes de compra y proveedores\n\n" +
            "Incluye autenticación JWT, control de acceso por roles (ADMIN, VETERINARIO, CLIENTE), " +
            "registro de usuarios con mascotas, y gestión completa del flujo de insumos veterinarios.";

        return new OpenAPI()
                .info(new Info()
                        .title("PetStore API - E-Commerce & Vet-Stock")
                        .version("1.0.0")
                        .description(description)
                        .contact(new Contact()
                                .name("Equipo 4")
                                .email("soporte@petstore.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer"))
                .components(new Components()
                        .addSecuritySchemes("Bearer", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Ingrese el token JWT")));
    }
}
