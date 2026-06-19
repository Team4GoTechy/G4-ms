package com.team4.petstore.event;

public class CitaCompletadaEvent {

    private final Long citaId;

    public CitaCompletadaEvent(Long citaId) {
        this.citaId = citaId;
    }

    public Long getCitaId() { return citaId; }
}