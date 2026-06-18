package com.team4.petstore.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CompraResponse {
    private Long id;
    private Long usuarioId;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String metodoPago;
    private String estado;
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
        private Long productoId;
        private String nombre;
        private Integer cantidad;
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
