package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public class EvolucionRequest {

    @Schema(description = "Observación clínica registrada por el veterinario", example = "Paciente alerta, come con normalidad, sin signos de dolor")
    private String observacion;
    @Schema(description = "Peso actualizado de la mascota en kg", example = "12.3")
    private BigDecimal peso;
    @Schema(description = "Temperatura actualizada en °C", example = "38.4")
    private BigDecimal temperatura;

    public EvolucionRequest() {}

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }
    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }
}
