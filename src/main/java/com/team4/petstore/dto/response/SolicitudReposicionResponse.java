package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Respuesta con datos de una solicitud de reposicion")
public class SolicitudReposicionResponse {

    @Schema(description = "ID de la solicitud", example = "1")
    private Long id;

    @Schema(description = "ID del veterinario", example = "3")
    private Long veterinarioId;

    @Schema(description = "Nombre del veterinario", example = "Doctor Veterinario")
    private String nombreVeterinario;

    @Schema(description = "Estado de la solicitud", example = "PENDIENTE")
    private String estado;

    @Schema(description = "Fecha de creacion")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Lista de insumos solicitados")
    private List<DetalleResponse> detalles;

    public SolicitudReposicionResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Long veterinarioId) { this.veterinarioId = veterinarioId; }
    public String getNombreVeterinario() { return nombreVeterinario; }
    public void setNombreVeterinario(String nombreVeterinario) { this.nombreVeterinario = nombreVeterinario; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public List<DetalleResponse> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleResponse> detalles) { this.detalles = detalles; }

    public static class DetalleResponse {
        private Long insumoId;
        private String nombreInsumo;
        private Integer cantidadSolicitada;

        public DetalleResponse() {}

        public DetalleResponse(Long insumoId, String nombreInsumo, Integer cantidadSolicitada) {
            this.insumoId = insumoId;
            this.nombreInsumo = nombreInsumo;
            this.cantidadSolicitada = cantidadSolicitada;
        }

        public Long getInsumoId() { return insumoId; }
        public void setInsumoId(Long insumoId) { this.insumoId = insumoId; }
        public String getNombreInsumo() { return nombreInsumo; }
        public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }
        public Integer getCantidadSolicitada() { return cantidadSolicitada; }
        public void setCantidadSolicitada(Integer cantidadSolicitada) { this.cantidadSolicitada = cantidadSolicitada; }
    }
}
