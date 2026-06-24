package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class ServicioResponse {

    @Schema(description = "ID del servicio", example = "1")
    private Long id;
    @Schema(description = "Nombre del servicio", example = "Consulta general")
    private String nombre;
    @Schema(description = "Descripción del servicio")
    private String descripcion;
    @Schema(description = "Precio del servicio", example = "5000.0")
    private Double precio;
    @Schema(description = "Veterinarios que ofrecen este servicio (poblado en GET /api/v1/servicios)")
    private List<VeterinarioResponse> veterinarios;

    public ServicioResponse(Long id, String nombre, String descripcion, Double precio,
                            List<VeterinarioResponse> veterinarios) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.setVeterinarios(veterinarios);
    }

    public ServicioResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public List<VeterinarioResponse> getVeterinarios() { return veterinarios; }
    public void setVeterinarios(List<VeterinarioResponse> veterinarios) { this.veterinarios = veterinarios; }
}
