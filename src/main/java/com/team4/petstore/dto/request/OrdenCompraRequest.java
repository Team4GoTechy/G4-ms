package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "Solicitud para crear una orden de compra a un proveedor")
public class OrdenCompraRequest {

    @Schema(description = "ID del proveedor", example = "1")
    @NotNull(message = "El ID del proveedor es obligatorio")
    private Long proveedorId;

    @Schema(description = "Lista de insumos a ordenar")
    @NotEmpty(message = "Debe incluir al menos un item")
    private List<OrdenItemRequest> items;

    public OrdenCompraRequest() {}

    public Long getProveedorId() { return proveedorId; }
    public void setProveedorId(Long proveedorId) { this.proveedorId = proveedorId; }
    public List<OrdenItemRequest> getItems() { return items; }
    public void setItems(List<OrdenItemRequest> items) { this.items = items; }

    public static class OrdenItemRequest {
        @Schema(description = "ID del insumo", example = "1")
        @NotNull(message = "El ID del insumo es obligatorio")
        private Long insumoId;

        @Schema(description = "Cantidad a ordenar", example = "50")
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a 0")
        private Integer cantidad;

        public OrdenItemRequest() {}

        public Long getInsumoId() { return insumoId; }
        public void setInsumoId(Long insumoId) { this.insumoId = insumoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}
