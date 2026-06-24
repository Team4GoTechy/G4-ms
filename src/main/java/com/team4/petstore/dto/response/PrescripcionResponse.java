package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;

public class PrescripcionResponse {

    @Schema(description = "ID de la prescripción", example = "1")
    private Long id;
    @Schema(description = "ID de la consulta asociada", example = "1")
    private Long consultaId;
    @Schema(description = "ID del veterinario que emitió la prescripción", example = "3")
    private Long veterinarioId;
    @Schema(description = "Nombre del veterinario", example = "Juan Perez")
    private String veterinarioNombre;
    @Schema(description = "Fecha de la prescripción")
    private LocalDate fecha;
    @Schema(description = "Observaciones generales")
    private String observaciones;
    @Schema(description = "Detalle de medicamentos recetados")
    private List<DetallePrescripcionResponse> detalles;

    public PrescripcionResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getConsultaId() { return consultaId; }
    public void setConsultaId(Long consultaId) { this.consultaId = consultaId; }
    public Long getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Long veterinarioId) { this.veterinarioId = veterinarioId; }
    public String getVeterinarioNombre() { return veterinarioNombre; }
    public void setVeterinarioNombre(String veterinarioNombre) { this.veterinarioNombre = veterinarioNombre; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public List<DetallePrescripcionResponse> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePrescripcionResponse> detalles) { this.detalles = detalles; }
}
