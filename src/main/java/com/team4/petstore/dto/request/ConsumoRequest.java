package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Solicitud para registrar consumo de insumos")
public class ConsumoRequest {

    @Schema(description = "Lista de insumos consumidos", example = "[{\"insumoId\": 1, \"cantidad\": 5}]")
    @NotNull(message = "Debe incluir al menos un insumo")
    private List<ConsumoItem> items;

    @Schema(description = "Descripcion del motivo del consumo", example = "Consulta de revision general")
    private String descripcion;

    public ConsumoRequest() {}

    public List<ConsumoItem> getItems() { return items; }
    public void setItems(List<ConsumoItem> items) { this.items = items; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public static class ConsumoItem {
        @NotNull(message = "El ID del insumo es obligatorio")
        private Long insumoId;

        @NotNull(message = "La cantidad es obligatoria")
        @jakarta.validation.constraints.Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer cantidad;

        public ConsumoItem() {}

        public Long getInsumoId() { return insumoId; }
        public void setInsumoId(Long insumoId) { this.insumoId = insumoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}
