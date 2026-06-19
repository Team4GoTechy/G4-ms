package com.team4.petstore.event;

public class EvolucionRegistradaEvent {

    private final Long internacionId;
    private final Long evolucionId;

    public EvolucionRegistradaEvent(Long internacionId, Long evolucionId) {
        this.internacionId = internacionId;
        this.evolucionId = evolucionId;
    }

    public Long getInternacionId() { return internacionId; }
    public Long getEvolucionId() { return evolucionId; }
}