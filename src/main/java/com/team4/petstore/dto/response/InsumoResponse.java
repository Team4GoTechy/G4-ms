package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Respuesta con datos del insumo")
public class InsumoResponse {

    @Schema(description = "ID del insumo", example = "1")
    private Long id;

    @Schema(description = "Nombre del insumo", example = "Vacuna Antirrabica")
    private String nombre;

    @Schema(description = "Descripcion del insumo", example = "Vacuna para prevencion de rabia")
    private String descripcion;

    @Schema(description = "Unidad de medida", example = "unidades")
    private String unidadMedida;

    @Schema(description = "Precio unitario", example = "25.50")
    private BigDecimal precioUnitario;

    @Schema(description = "Stock minimo para alertas", example = "10")
    private Integer stockMinimo;

    @Schema(description = "Indica si esta activo", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de creacion")
    private LocalDateTime fechaCreacion;

    public InsumoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
