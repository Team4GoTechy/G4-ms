package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EvolucionResponse {

    @Schema(description = "ID de la evolución", example = "1")
    private Long id;
    @Schema(description = "ID de la internación asociada", example = "1")
    private Long internacionId;
    @Schema(description = "ID del usuario que registró la evolución", example = "3")
    private Long usuarioId;
    @Schema(description = "Nombre del usuario que registró la evolución", example = "Juan Perez")
    private String usuarioNombre;
    @Schema(description = "Fecha y hora del registro")
    private LocalDateTime fechaHora;
    @Schema(description = "Observación clínica")
    private String observacion;
    @Schema(description = "Peso registrado en kg")
    private BigDecimal peso;
    @Schema(description = "Temperatura registrada en °C")
    private BigDecimal temperatura;

    public EvolucionResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInternacionId() { return internacionId; }
    public void setInternacionId(Long internacionId) { this.internacionId = internacionId; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }
    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }
}
