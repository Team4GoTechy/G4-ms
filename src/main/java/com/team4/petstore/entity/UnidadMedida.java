package com.team4.petstore.entity;

public enum UnidadMedida {
    G("g"),
    KG("kg"),
    ML("ml"),
    L("L"),
    UNIDAD("unidad");

    private final String simbolo;

    UnidadMedida(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
