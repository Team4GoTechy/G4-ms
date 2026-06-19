package com.team4.petstore.event;

public class CitaCreadaEvent {

    private final Long citaId;

    public CitaCreadaEvent(Long citaId) {
        this.citaId = citaId;
    }

    public Long getCitaId() { return citaId; }
}