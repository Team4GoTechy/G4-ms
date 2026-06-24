package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CitaRequest {

    @Schema(description = "ID de la mascota", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long mascotaId;

    @Schema(description = "ID del veterinario", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long veterinarioId;

    @NotNull(message = "El servicio es obligatorio")
    @Schema(description = "ID del servicio a agendar. El backend valida que el veterinario lo ofrezca", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long servicioId;

    @Schema(description = "Fecha y hora de la cita en formato ISO 8601", example = "2026-07-15T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime fechaHora;

    @Schema(description = "Duración estimada en minutos", example = "30", defaultValue = "30")
    private Integer duracionMinutos;

    @Schema(description = "Notas adicionales sobre la cita", example = "Control de rutina")
    private String notas;

    public CitaRequest() {}

    public Long getMascotaId() { return mascotaId; }
    public void setMascotaId(Long mascotaId) { this.mascotaId = mascotaId; }

    public Long getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Long veterinarioId) { this.veterinarioId = veterinarioId; }

    public Long getServicioId() { return servicioId; }
    public void setServicioId(Long servicioId) { this.servicioId = servicioId; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
