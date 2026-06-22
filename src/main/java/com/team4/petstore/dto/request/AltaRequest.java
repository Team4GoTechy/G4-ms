package com.team4.petstore.dto.request;

public class AltaRequest {

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
