package com.team4.petstore.dto.request;

public class InternacionRequest {

    private Long mascotaId;
    private Long veterinarioId;
    private String motivo;
    private String jaulaId;
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