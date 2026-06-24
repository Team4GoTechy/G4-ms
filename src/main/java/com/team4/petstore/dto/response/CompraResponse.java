package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CompraResponse {

    @Schema(description = "ID de la compra", example = "1")
    private Long id;
    @Schema(description = "ID del usuario que realizó la compra", example = "7")
    private Long usuarioId;
    @Schema(description = "Fecha y hora de la compra")
    private LocalDateTime fecha;
    @Schema(description = "Monto total de la compra", example = "15999.50")
    private BigDecimal total;
    @Schema(description = "Método de pago utilizado")
    private String metodoPago;
    @Schema(description = "Estado actual de la compra", example = "CONFIRMADO", allowableValues = {"PENDIENTE", "CONFIRMADO", "ENTREGADO", "CANCELADO"})
    private String estado;
    @Schema(description = "Detalle de productos comprados")
    private List<DetalleResponse> productos;

    public CompraResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public List<DetalleResponse> getProductos() { return productos; }
    public void setProductos(List<DetalleResponse> productos) { this.productos = productos; }

    public static class DetalleResponse {

        @Schema(description = "ID del producto", example = "1")
        private Long productoId;
        @Schema(description = "Nombre del producto", example = "Alimento balanceado 15kg")
        private String nombre;
        @Schema(description = "Cantidad comprada", example = "2")
        private Integer cantidad;
        @Schema(description = "Precio unitario al momento de la compra", example = "7999.75")
        private BigDecimal precioUnitario;

        public DetalleResponse() {}

        public DetalleResponse(Long productoId, String nombre, Integer cantidad, BigDecimal precioUnitario) {
            this.productoId = productoId;
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
        }

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    }
}
