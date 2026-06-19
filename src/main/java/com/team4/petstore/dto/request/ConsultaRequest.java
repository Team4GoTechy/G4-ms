package com.team4.petstore.dto.request;
import java.math.BigDecimal;

public class ConsultaRequest {

    private Long citaId;
    private Long mascotaId;
    private Long veterinarioId;
    private String motivo;
    private String anamnesis;
    private String examenFisico;
    private String diagnostico;
    private String tratamiento;
    private BigDecimal peso;
    private BigDecimal temperatura;
    private Integer frecuenciaCardiaca;
    private Integer frecuenciaRespiratoria;
    private String trc;
    private String notas;

    public ConsultaRequest() {}

    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }

    public Long getMascotaId() { return mascotaId; }
    public void setMascotaId(Long mascotaId) { this.mascotaId = mascotaId; }

    public Long getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Long veterinarioId) { this.veterinarioId = veterinarioId; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getAnamnesis() { return anamnesis; }
    public void setAnamnesis(String anamnesis) { this.anamnesis = anamnesis; }

    public String getExamenFisico() { return examenFisico; }
    public void setExamenFisico(String examenFisico) { this.examenFisico = examenFisico; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }

    public BigDecimal getPeso() { return peso; }
    public void setPeso(BigDecimal peso) { this.peso = peso; }

    public BigDecimal getTemperatura() { return temperatura; }
    public void setTemperatura(BigDecimal temperatura) { this.temperatura = temperatura; }

    public Integer getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(Integer frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }

    public Integer getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public void setFrecuenciaRespiratoria(Integer frecuenciaRespiratoria) { this.frecuenciaRespiratoria = frecuenciaRespiratoria; }

    public String getTrc() { return trc; }
    public void setTrc(String trc) { this.trc = trc; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}