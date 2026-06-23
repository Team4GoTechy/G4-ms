package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitud para cambiar el estado de un veterinario")
public class CambiarEstadoRequest {

    @Schema(description = "Indica si el veterinario está activo", example = "true")
    @NotNull(message = "El estado es obligatorio")
    private Boolean activo;

    public CambiarEstadoRequest() {}

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
