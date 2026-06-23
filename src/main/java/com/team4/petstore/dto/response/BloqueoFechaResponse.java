package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Respuesta con datos del bloqueo de fecha")
public class BloqueoFechaResponse {

    @Schema(description = "ID del bloqueo", example = "1")
    private Long id;

    @Schema(description = "Fecha de inicio", example = "2024-07-10")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin", example = "2024-07-15")
    private LocalDate fechaFin;

    @Schema(description = "Motivo del bloqueo", example = "Vacaciones anuales")
    private String motivo;

    @Schema(description = "Fecha de creación del bloqueo", example = "2024-06-01T10:00:00")
    private LocalDateTime createdAt;

    public BloqueoFechaResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
