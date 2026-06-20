package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Respuesta con datos de una orden de compra")
public class OrdenCompraResponse {

    @Schema(description = "ID de la orden", example = "1")
    private Long id;

    @Schema(description = "ID del proveedor", example = "1")
    private Long proveedorId;

    @Schema(description = "Nombre del proveedor", example = "Distribuidora Medica ABC")
    private String nombreProveedor;

    @Schema(description = "Estado de la orden", example = "PENDIENTE")
    private String estado;

    @Schema(description = "Fecha de creacion")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Lista de items")
    private List<DetalleResponse> detalles;

    @Schema(description = "Total de la orden", example = "1250.00")
    private BigDecimal total;

    public OrdenCompraResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProveedorId() { return proveedorId; }
    public void setProveedorId(Long proveedorId) { this.proveedorId = proveedorId; }
    public String getNombreProveedor() { return nombreProveedor; }
    public void setNombreProveedor(String nombreProveedor) { this.nombreProveedor = nombreProveedor; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public List<DetalleResponse> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleResponse> detalles) { this.detalles = detalles; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public static class DetalleResponse {
        private Long insumoId;
        private String nombreInsumo;
        private Integer cantidad;
        private BigDecimal precioUnitario;
        private BigDecimal subtotal;

        public DetalleResponse() {}

        public DetalleResponse(Long insumoId, String nombreInsumo, Integer cantidad, BigDecimal precioUnitario) {
            this.insumoId = insumoId;
            this.nombreInsumo = nombreInsumo;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }

        public Long getInsumoId() { return insumoId; }
        public void setInsumoId(Long insumoId) { this.insumoId = insumoId; }
        public String getNombreInsumo() { return nombreInsumo; }
        public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }
        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public BigDecimal getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    }
}
