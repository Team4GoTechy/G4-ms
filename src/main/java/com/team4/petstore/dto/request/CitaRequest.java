package com.team4.petstore.dto.request;

import com.team4.petstore.entity.enums.TipoCita;
import java.time.LocalDateTime;

public class CitaRequest {

    private Long mascotaId;
    private Long veterinarioId;
    private TipoCita tipoCita;
    private LocalDateTime fechaHora;
    private Integer duracionMinutos;
    private String notas;

    public CitaRequest() {}

    public Long getMascotaId() { return mascotaId; }
    public void setMascotaId(Long mascotaId) { this.mascotaId = mascotaId; }

    public Long getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Long veterinarioId) { this.veterinarioId = veterinarioId; }

    public TipoCita getTipoCita() { return tipoCita; }
    public void setTipoCita(TipoCita tipoCita) { this.tipoCita = tipoCita; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}