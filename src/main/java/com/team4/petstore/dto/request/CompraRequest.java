package com.team4.petstore.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class CompraRequest {

    @NotNull(message = "El método de pago es obligatorio")
    private String metodoPago;

    @NotEmpty(message = "Debe incluir al menos un producto")
    private java.util.List<ItemCompraRequest> productos;

    public CompraRequest() {}

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public java.util.List<ItemCompraRequest> getProductos() { return productos; }
    public void setProductos(java.util.List<ItemCompraRequest> productos) { this.productos = productos; }

    public static class ItemCompraRequest {
        @NotNull(message = "El productoId es obligatorio")
        private Long productoId;

        @NotNull(message = "La cantidad es obligatoria")
        @jakarta.validation.constraints.Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer cantidad;

        public ItemCompraRequest() {}

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    }
}
