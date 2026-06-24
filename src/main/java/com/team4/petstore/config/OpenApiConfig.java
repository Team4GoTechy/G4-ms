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
        final String description = "API REST completa para Pet Store. Incluye cuatro módulos principales:\n" +
            "- **E-Commerce**: Gestión de productos, categorías, compras y usuarios (roles CLIENTE, ADMIN)\n" +
            "- **Gestión Veterinaria**: Mascotas, historial clínico, consultas, prescripciones, citas, internaciones, notificaciones y archivos clínicos (roles ADMIN, VETERINARIO, CLIENTE)\n" +
            "- **Vet-Stock**: Gestión de insumos médicos, stock, solicitudes de reposición, órdenes de compra, proveedores y consumo (roles ADMIN, VETERINARIO)\n" +
            "- **Vet-Admin**: Alta y administración de veterinarios, catálogo de servicios, horarios de atención y bloqueos de fecha (rol ADMIN)\n\n" +
            "Incluye autenticación JWT, control de acceso por roles (ADMIN, VETERINARIO/DOCTOR, CLIENTE), " +
            "registro de usuarios con mascotas, gestión completa del flujo clínico y de stock veterinario, " +
            "y upload de imágenes a Cloudinary.";

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
