package com.team4.petstore.dto.response;

import com.team4.petstore.entity.enums.EstadoInternacion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public class InternacionResponse {

    @Schema(description = "ID de la internación", example = "1")
    private Long id;
    @Schema(description = "ID de la mascota", example = "1")
    private Long mascotaId;
    @Schema(description = "Nombre de la mascota", example = "Max")
    private String mascotaNombre;
    @Schema(description = "ID del veterinario", example = "3")
    private Long veterinarioId;
    @Schema(description = "Nombre del veterinario", example = "Juan Perez")
    private String veterinarioNombre;
    @Schema(description = "Motivo de la internación")
    private String motivo;
    @Schema(description = "Fecha y hora de ingreso")
    private LocalDateTime fechaIngreso;
    @Schema(description = "Fecha y hora de alta (si aplica)")
    private LocalDateTime fechaAlta;
    @Schema(description = "ID de la jaula asignada", example = "J-12")
    private String jaulaId;
    @Schema(description = "Estado actual de la internación")
    private EstadoInternacion estado;
    @Schema(description = "Notas iniciales")
    private String notas;
    @Schema(description = "Indicaciones registradas al dar el alta")
    private String indicacionesAlta;
    @Schema(description = "Notas del cliente para un eventual reingreso")
    private String notasCliente;
    @Schema(description = "Listado de evoluciones clínicas registradas (ordenadas por fechaHora asc)")
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
    public String getIndicacionesAlta() { return indicacionesAlta; }
    public void setIndicacionesAlta(String indicacionesAlta) { this.indicacionesAlta = indicacionesAlta; }
    public String getNotasCliente() { return notasCliente; }
    public void setNotasCliente(String notasCliente) { this.notasCliente = notasCliente; }
    public List<EvolucionResponse> getEvoluciones() { return evoluciones; }
    public void setEvoluciones(List<EvolucionResponse> evoluciones) { this.evoluciones = evoluciones; }
}
