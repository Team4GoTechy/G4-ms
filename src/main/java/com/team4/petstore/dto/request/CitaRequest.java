package com.team4.petstore.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class CitaRequest {

    private Long mascotaId;
    private Long veterinarioId;

    @NotNull(message = "El servicio es obligatorio")
    private Long servicioId;

    private LocalDateTime fechaHora;
    private Integer duracionMinutos;
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