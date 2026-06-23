package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(description = "Solicitud para crear un bloqueo de fechas")
public class BloqueoFechaRequest {

    @Schema(description = "Fecha de inicio del bloqueo", example = "2024-07-10")
    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin del bloqueo", example = "2024-07-15")
    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    @Schema(description = "Motivo del bloqueo (opcional)", example = "Vacaciones anuales")
    @Size(max = 255, message = "El motivo no puede exceder 255 caracteres")
    private String motivo;

    public BloqueoFechaRequest() {}

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
