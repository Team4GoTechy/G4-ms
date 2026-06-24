package com.team4.petstore.dto.response;

import com.team4.petstore.entity.enums.EstadoCita;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class CitaResponse {

    @Schema(description = "ID de la cita", example = "1")
    private Long id;
    @Schema(description = "ID de la mascota", example = "1")
    private Long mascotaId;
    @Schema(description = "Nombre de la mascota", example = "Max")
    private String mascotaNombre;
    @Schema(description = "ID del veterinario", example = "3")
    private Long veterinarioId;
    @Schema(description = "Nombre del veterinario", example = "Juan Perez")
    private String veterinarioNombre;
    @Schema(description = "URL del avatar del veterinario")
    private String veterinarioAvatar;
    @Schema(description = "ID del cliente dueño de la mascota", example = "7")
    private Long clienteId;
    @Schema(description = "Nombre del cliente", example = "Cliente Demo")
    private String clienteNombre;
    @Schema(description = "Servicio agendado (objeto completo)")
    private ServicioResponse servicio;
    @Schema(description = "Fecha y hora de la cita en formato ISO 8601", example = "2026-07-15T10:00:00")
    private LocalDateTime fechaHora;
    @Schema(description = "Duración en minutos", example = "30")
    private Integer duracionMinutos;
    @Schema(description = "Estado actual de la cita")
    private EstadoCita estado;
    @Schema(description = "Notas adicionales")
    private String notas;
    @Schema(description = "Motivo de cancelación si aplica")
    private String motivoCancelacion;
    @Schema(description = "Fecha de creación de la cita")
    private LocalDateTime fechaCreacion;
    @Schema(description = "Indica si la cita fue pagada", example = "false")
    private Boolean pagado;

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
    public String getVeterinarioAvatar() { return veterinarioAvatar; }
    public void setVeterinarioAvatar(String veterinarioAvatar) { this.veterinarioAvatar = veterinarioAvatar; }
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }
    public ServicioResponse getServicio() { return servicio; }
    public void setServicio(ServicioResponse servicio) { this.servicio = servicio; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public Integer getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(Integer duracionMinutos) { this.duracionMinutos = duracionMinutos; }
    public EstadoCita getEstado() { return estado; }
    public void setEstado(EstadoCita estado) { this.estado = estado; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
    public String getMotivoCancelacion() { return motivoCancelacion; }
    public void setMotivoCancelacion(String motivoCancelacion) { this.motivoCancelacion = motivoCancelacion; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion)  { this.fechaCreacion = fechaCreacion; }
    public Boolean getPagado() { return pagado; }
    public void setPagado(Boolean pagado) { this.pagado = pagado; }
}
