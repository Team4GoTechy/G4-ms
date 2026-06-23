package com.team4.petstore.dto.request;

import jakarta.validation.constraints.NotNull;

public class ReingresoRequest {

    @NotNull(message = "El ID de la mascota es obligatorio")
    private Long mascotaId;

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
