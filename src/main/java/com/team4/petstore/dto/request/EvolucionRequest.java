package com.team4.petstore.dto.request;

import java.math.BigDecimal;

public class EvolucionRequest {

    private String observacion;
    private BigDecimal peso;
    private BigDecimal temperatura;

    public EvolucionRequest() {}

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }
}