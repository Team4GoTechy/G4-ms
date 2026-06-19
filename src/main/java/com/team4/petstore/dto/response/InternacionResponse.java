package com.team4.petstore.dto.response;
import com.team4.petstore.entity.enums.EstadoInternacion;
import java.time.LocalDateTime;
import java.util.List;

public class InternacionResponse {

    private Long id;
    private Long mascotaId;
    private String mascotaNombre;
    private Long veterinarioId;
    private String veterinarioNombre;
    private String motivo;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaAlta;
    private String jaulaId;
    private EstadoInternacion estado;
    private String notas;
    private List<EvolucionResponse> evoluciones;

    public InternacionResponse() {}

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

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }

    public String getJaulaId() { return jaulaId; }
    public void setJaulaId(String jaulaId) { this.jaulaId = jaulaId; }

    public EstadoInternacion getEstado() { return estado; }
    public void setEstado(EstadoInternacion estado) { this.estado = estado; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public List<EvolucionResponse> getEvoluciones() { return evoluciones; }
    public void setEvoluciones(List<EvolucionResponse> evoluciones) { this.evoluciones = evoluciones; }
}