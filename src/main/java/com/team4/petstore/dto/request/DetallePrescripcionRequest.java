package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DetallePrescripcionRequest {

    @NotNull(message = "El insumo es obligatorio")
    @Schema(description = "ID del insumo/medicamento recetado", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long insumoId;

    @NotBlank(message = "La dosis es obligatoria")
    @Schema(description = "Dosis a administrar", example = "1 comprimido", requiredMode = Schema.RequiredMode.REQUIRED)
    private String dosis;

    @NotBlank(message = "La frecuencia es obligatoria")
    @Schema(description = "Frecuencia de administración", example = "Cada 12 horas", requiredMode = Schema.RequiredMode.REQUIRED)
    private String frecuencia;

    @NotBlank(message = "La duración es obligatoria")
    @Schema(description = "Duración del tratamiento", example = "7 días", requiredMode = Schema.RequiredMode.REQUIRED)
    private String duracion;

    @Schema(description = "Vía de administración", example = "Oral")
    private String viaAdministracion;
    @Schema(description = "Instrucciones especiales", example = "Administrar con el alimento")
    private String instrucciones;

    public DetallePrescripcionRequest() {}

    public Long getInsumoId() { return insumoId; }
    public void setInsumoId(Long insumoId) { this.insumoId = insumoId; }
    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }
    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }
    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    public String getViaAdministracion() { return viaAdministracion; }
    public void setViaAdministracion(String viaAdministracion) { this.viaAdministracion = viaAdministracion; }
    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }
}
