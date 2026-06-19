package com.team4.petstore.dto.response;
import java.time.LocalDate;
import java.util.List;

public class PrescripcionResponse {

    private Long id;
    private Long consultaId;
    private Long veterinarioId;
    private String veterinarioNombre;
    private LocalDate fecha;
    private String observaciones;
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