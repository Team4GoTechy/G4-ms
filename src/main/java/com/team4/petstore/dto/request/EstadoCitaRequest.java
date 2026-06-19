package com.team4.petstore.dto.request;
import com.team4.petstore.entity.enums.EstadoCita;

public class EstadoCitaRequest {

    private EstadoCita estado;

    public EstadoCitaRequest() {}

    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
}