package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CompraRequest {

    @NotNull(message = "El método de pago es obligatorio")
    @Schema(description = "Método de pago utilizado", example = "TARJETA", allowableValues = {"TRANSFERENCIA", "EFECTIVO", "TARJETA"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String metodoPago;

    @NotEmpty(message = "Debe incluir al menos un producto")
    @Schema(description = "Lista de productos a comprar", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ItemCompraRequest> productos;

    public CompraRequest() {}

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public List<ItemCompraRequest> getProductos() { return productos; }
    public void setProductos(List<ItemCompraRequest> productos) { this.productos = productos; }

    public static class ItemCompraRequest {

        @NotNull(message = "El productoId es obligatorio")
        @Schema(description = "ID del producto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        private Long productoId;

        @NotNull(message = "La cantidad es obligatoria")
        @jakarta.validation.constraints.Min(value = 1, message = "La cantidad debe ser al menos 1")
        @Schema(description = "Cantidad del producto", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        private Integer cantidad;

        public ItemCompraRequest() {}

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}
