package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public class DetallePrescripcionResponse {

    @Schema(description = "ID del detalle", example = "1")
    private Long id;
    @Schema(description = "ID del insumo", example = "1")
    private Long insumoId;
    @Schema(description = "Nombre del insumo", example = "Antibiotico Amoxicilina")
    private String insumoNombre;
    @Schema(description = "Dosis indicada", example = "1 comprimido")
    private String dosis;
    @Schema(description = "Frecuencia de administración", example = "Cada 12 horas")
    private String frecuencia;
    @Schema(description = "Duración del tratamiento", example = "7 días")
    private String duracion;
    @Schema(description = "Vía de administración", example = "Oral")
    private String viaAdministracion;
    @Schema(description = "Instrucciones especiales")
    private String instrucciones;

    public DetallePrescripcionResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInsumoId() { return insumoId; }
    public void setInsumoId(Long insumoId) { this.insumoId = insumoId; }
    public String getInsumoNombre() { return insumoNombre; }
    public void setInsumoNombre(String insumoNombre) { this.insumoNombre = insumoNombre; }
    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public String getViaAdministracion() { return viaAdministracion; }
    public void setViaAdministracion(String viaAdministracion) { this.viaAdministracion = viaAdministracion; }
    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }
}
