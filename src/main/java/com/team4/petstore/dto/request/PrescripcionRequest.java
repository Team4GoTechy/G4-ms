package com.team4.petstore.dto.request;

import java.util.List;

public class PrescripcionRequest {

    private Long consultaId;
    private String observaciones;
    private List<DetallePrescripcionRequest> detalles;

    public PrescripcionRequest() {}

    public Long getConsultaId() { return consultaId; }
    public void setConsultaId(Long consultaId) { this.consultaId = consultaId; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public List<DetallePrescripcionRequest> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePrescripcionRequest> detalles) { this.detalles = detalles; }
}