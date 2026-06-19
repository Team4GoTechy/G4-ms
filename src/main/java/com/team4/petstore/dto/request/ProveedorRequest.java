package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Solicitud para crear o actualizar un proveedor")
public class ProveedorRequest {

    @Schema(description = "Nombre del proveedor", example = "Distribuidora Medica ABC")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    private String nombre;

    @Schema(description = "Email del proveedor", example = "ventas@distmedica.com")
    private String email;

    @Schema(description = "Telefono del proveedor", example = "351-555-1234")
    private String telefono;

    public ProveedorRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
