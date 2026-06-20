package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Respuesta con datos de movimiento de insumo")
public class MovimientoInsumoResponse {

    @Schema(description = "ID del movimiento", example = "1")
    private Long id;

    @Schema(description = "ID del insumo", example = "1")
    private Long insumoId;

    @Schema(description = "Nombre del insumo", example = "Vacuna Antirrabica")
    private String nombreInsumo;

    @Schema(description = "Tipo de movimiento", example = "ENTRADA")
    private String tipo;

    @Schema(description = "Cantidad", example = "50")
    private Integer cantidad;

    @Schema(description = "Precio unitario", example = "22.50")
    private BigDecimal precioUnitario;

    @Schema(description = "Fecha del movimiento")
    private LocalDateTime fecha;

    @Schema(description = "Descripcion", example = "Compra a Distribuidora Medica ABC")
    private String descripcion;

    @Schema(description = "ID de referencia (orden de compra)", example = "1")
    private Long referenciaId;

    public MovimientoInsumoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getInsumoId() { return insumoId; }
    public void setInsumoId(Long insumoId) { this.insumoId = insumoId; }
    public String getNombreInsumo() { return nombreInsumo; }
    public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }
}
