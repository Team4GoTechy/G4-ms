package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public class InternacionRequest {

    @Schema(description = "ID de la mascota a internar", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long mascotaId;
    @Schema(description = "ID del veterinario responsable", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long veterinarioId;
    @Schema(description = "Motivo de la internación", example = "Cirugía abdominal programada")
    private String motivo;
    @Schema(description = "ID de la jaula asignada", example = "J-12")
    private String jaulaId;
    @Schema(description = "Notas iniciales de la internación")
    private String notas;

    public InternacionRequest() {}

    public Long getMascotaId() { return mascotaId; }
    public void setMascotaId(Long mascotaId) { this.mascotaId = mascotaId; }
    public Long getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Long veterinarioId) { this.veterinarioId = veterinarioId; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getJaulaId() { return jaulaId; }
    public void setJaulaId(String jaulaId) { this.jaulaId = jaulaId; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}
