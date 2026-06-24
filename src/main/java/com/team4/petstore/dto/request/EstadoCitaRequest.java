package com.team4.petstore.dto.request;

import com.team4.petstore.entity.enums.EstadoCita;
import io.swagger.v3.oas.annotations.media.Schema;

public class EstadoCitaRequest {

    @Schema(description = "Nuevo estado de la cita", example = "CONFIRMADA", requiredMode = Schema.RequiredMode.REQUIRED)
    private EstadoCita estado;

    @Schema(description = "Motivo del cambio de estado. Obligatorio si estado es CANCELADA. Se guarda en cita.motivoCancelacion", example = "Cliente no puede asistir por viaje")
    private String motivo;

    public EstadoCitaRequest() {}

    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}
