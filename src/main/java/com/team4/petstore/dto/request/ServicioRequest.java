package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ServicioRequest {

    @NotBlank
    @Schema(description = "Nombre del servicio", example = "Consulta general", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @Schema(description = "Descripción del servicio", example = "Revisión clínica completa de la mascota")
    private String descripcion;

    @NotNull
    @Schema(description = "Precio del servicio en pesos", example = "5000.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double precio;

    @Schema(description = "Lista de IDs de veterinarios que ofrecerán este servicio", example = "[1, 2, 3]")
    private List<Long> veterinarioIds;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public List<Long> getVeterinarioIds() { return veterinarioIds; }
    public void setVeterinarioIds(List<Long> veterinarioIds) { this.veterinarioIds = veterinarioIds; }
}
