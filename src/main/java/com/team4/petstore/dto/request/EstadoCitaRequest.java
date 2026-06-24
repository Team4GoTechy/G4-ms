package com.team4.petstore.dto.request;
import com.team4.petstore.entity.enums.EstadoCita;

public class EstadoCitaRequest {

    private EstadoCita estado;
    private String motivo;

    public EstadoCitaRequest() {}

    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
}