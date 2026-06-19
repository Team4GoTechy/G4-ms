package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Solicitud para completar una orden de compra (con precios reales)")
public class OrdenCompraCompletarRequest {

    @Schema(description = "Lista de items con precios reales", example = "[{\"insumoId\": 1, \"cantidad\": 50, \"precioUnitario\": 22.50}]")
    @NotEmpty(message = "Debe incluir al menos un item")
    private List<OrdenItem> items;

    public OrdenCompraCompletarRequest() {}

    public List<OrdenItem> getItems() { return items; }
    public void setItems(List<OrdenItem> items) { this.items = items; }

    public static class OrdenItem {
        @NotNull(message = "El ID del insumo es obligatorio")
        private Long insumoId;

        @NotNull(message = "La cantidad es obligatoria")
        private Integer cantidad;

        @NotNull(message = "El precio unitario es obligatorio")
        private BigDecimal precioUnitario;

        public OrdenItem() {}

        public Long getInsumoId() { return insumoId; }
        public void setInsumoId(Long insumoId) { this.insumoId = insumoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    }
}
