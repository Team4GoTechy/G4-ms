package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Respuesta con datos de stock de un insumo")
public class StockInsumoResponse {

    @Schema(description = "ID del insumo", example = "1")
    private Long insumoId;

    @Schema(description = "Nombre del insumo", example = "Vacuna Antirrabica")
    private String nombreInsumo;

    @Schema(description = "Cantidad actual en stock", example = "100")
    private Integer cantidadActual;

    @Schema(description = "Stock minimo para alertas", example = "10")
    private Integer stockMinimo;

    @Schema(description = "Indica si esta por debajo del stock minimo", example = "false")
    private Boolean alertaStock;

    @Schema(description = "Unidad de medida", example = "unidades")
    private String unidadMedida;

    @Schema(description = "Precio unitario actual", example = "25.50")
    private BigDecimal precioUnitario;

    public StockInsumoResponse() {}

    public Long getInsumoId() { return insumoId; }
    public void setInsumoId(Long insumoId) { this.insumoId = insumoId; }
    public String getNombreInsumo() { return nombreInsumo; }
    public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }
    public Integer getCantidadActual() { return cantidadActual; }
    public void setCantidadActual(Integer cantidadActual) { this.cantidadActual = cantidadActual; }
    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }
    public Boolean getAlertaStock() { return alertaStock; }
    public void setAlertaStock(Boolean alertaStock) { this.alertaStock = alertaStock; }
    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
}
