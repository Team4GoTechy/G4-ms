package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class ReingresoRequest {

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Schema(description = "ID de la mascota para la cual se solicita el reingreso", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long mascotaId;

    @Schema(description = "Notas del cliente que justifican la solicitud de reingreso", example = "La mascota presentó recaída de los síntomas")
    private String notasCliente;

    public ReingresoRequest() {}

    public ReingresoRequest(Long mascotaId, String notasCliente) {
        this.mascotaId = mascotaId;
        this.notasCliente = notasCliente;
    }

    public Long getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Long mascotaId) {
        this.mascotaId = mascotaId;
    }

    public String getNotasCliente() {
        return notasCliente;
    }

    public void setNotasCliente(String notasCliente) {
        this.notasCliente = notasCliente;
    }
}
