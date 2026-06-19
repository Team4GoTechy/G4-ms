package com.team4.petstore.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EvolucionResponse {

    private Long id;
    private Long internacionId;
    private Long usuarioId;
    private String usuarioNombre;
    private LocalDateTime fechaHora;
    private String observacion;
    private BigDecimal peso;
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