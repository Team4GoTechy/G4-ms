package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Solicitud para crear una solicitud de reposicion de insumos")
public class SolicitudReposicionRequest {

    @Schema(description = "Lista de insumos solicitados", example = "[{\"insumoId\": 1, \"cantidadSolicitada\": 50}]")
    @NotEmpty(message = "Debe incluir al menos un insumo")
    private List<DetalleSolicitudItem> detalles;

    public SolicitudReposicionRequest() {}

    public List<DetalleSolicitudItem> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleSolicitudItem> detalles) { this.detalles = detalles; }

    public static class DetalleSolicitudItem {
        @NotNull(message = "El ID del insumo es obligatorio")
        private Long insumoId;

        @NotNull(message = "La cantidad es obligatoria")
        @jakarta.validation.constraints.Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer cantidadSolicitada;

        public DetalleSolicitudItem() {}

        public Long getInsumoId() { return insumoId; }
        public void setInsumoId(Long insumoId) { this.insumoId = insumoId; }
        public Integer getCantidadSolicitada() { return cantidadSolicitada; }
        public void setCantidadSolicitada(Integer cantidadSolicitada) { this.cantidadSolicitada = cantidadSolicitada; }
    }
}
