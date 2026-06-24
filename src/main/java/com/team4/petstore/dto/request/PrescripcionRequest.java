package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class PrescripcionRequest {

    @Schema(description = "ID de la consulta asociada. Si se omite, el controller lo setea desde la URL", example = "1")
    private Long consultaId;
    @Schema(description = "Observaciones generales de la prescripción", example = "Administrar con el alimento")
    private String observaciones;
    @Schema(description = "Detalle de medicamentos/insumos recetados")
    private List<DetallePrescripcionRequest> detalles;

    public PrescripcionRequest() {}

    public Long getConsultaId() { return consultaId; }
    public void setConsultaId(Long consultaId) { this.consultaId = consultaId; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public List<DetallePrescripcionRequest> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePrescripcionRequest> detalles) { this.detalles = detalles; }
}
