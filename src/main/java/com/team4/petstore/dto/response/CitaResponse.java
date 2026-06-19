package com.team4.petstore.dto.response;
import com.team4.petstore.entity.enums.EstadoCita;
import com.team4.petstore.entity.enums.TipoCita;
import java.time.LocalDateTime;

public class CitaResponse {

    private Long id;
    private Long mascotaId;
    private String mascotaNombre;
    private Long veterinarioId;
    private String veterinarioNombre;
    private TipoCita tipoCita;
    private LocalDateTime fechaHora;
    private Integer duracionMinutos;
    private EstadoCita estado;
    private String notas;
    private LocalDateTime fechaCreacion;

    public CitaResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMascotaId() { return mascotaId; }
    public void setMascotaId(Long mascotaId) { this.mascotaId = mascotaId; }

    public String getMascotaNombre() { return mascotaNombre; }
    public void setMascotaNombre(String mascotaNombre) { this.mascotaNombre = mascotaNombre; }

    public Long getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Long veterinarioId) { this.veterinarioId = veterinarioId; }

    public String getVeterinarioNombre() { return veterinarioNombre; }
    public void setVeterinarioNombre(String veterinarioNombre) { this.veterinarioNombre = veterinarioNombre; }

    public TipoCita getTipoCita() { return tipoCita; }
    public void setTipoCita(TipoCita tipoCita) { this.tipoCita = tipoCita; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion)  { this.fechaCreacion = fechaCreacion; }
}
