package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Respuesta con datos del proveedor")
public class ProveedorResponse {

    @Schema(description = "ID del proveedor", example = "1")
    private Long id;

    @Schema(description = "Nombre del proveedor", example = "Distribuidora Medica ABC")
    private String nombre;

    @Schema(description = "Email del proveedor", example = "ventas@distmedica.com")
    private String email;

    @Schema(description = "Telefono del proveedor", example = "351-555-1234")
    private String telefono;

    @Schema(description = "Indica si esta activo", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de creacion")
    private LocalDateTime fechaCreacion;

    public ProveedorResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
