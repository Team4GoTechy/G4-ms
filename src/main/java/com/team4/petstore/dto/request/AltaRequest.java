package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class AltaRequest {

    @Schema(description = "Indicaciones o recomendaciones al momento de dar el alta", example = "Reposo por 7 días, control en 15 días")
    private String indicacionesAlta;

    public AltaRequest() {}

    public AltaRequest(String indicacionesAlta) {
        this.indicacionesAlta = indicacionesAlta;
    }

    public String getIndicacionesAlta() {
        return indicacionesAlta;
    }

    public void setIndicacionesAlta(String indicacionesAlta) {
        this.indicacionesAlta = indicacionesAlta;
    }
}
