package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public class ConsultaRequest {

    @Schema(description = "ID de la cita que origina la consulta (opcional)", example = "1")
    private Long citaId;
    @Schema(description = "ID de la mascota", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long mascotaId;
    @Schema(description = "ID del veterinario que realiza la consulta", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long veterinarioId;
    @Schema(description = "Motivo de la consulta", example = "Control de rutina")
    private String motivo;
    @Schema(description = "Anamnesis (historia clínica relatada por el dueño)", example = "Dueño reporta decaimiento y falta de apetito desde hace 3 días")
    private String anamnesis;
    @Schema(description = "Examen físico realizado", example = "Mucosas rosadas, hidratación normal, sin adenopatías")
    private String examenFisico;
    @Schema(description = "Diagnóstico presuntivo o definitivo", example = "Gastritis leve")
    private String diagnostico;
    @Schema(description = "Tratamiento indicado", example = "Dieta blanda por 48hs, omeprazol 20mg cada 12hs")
    private String tratamiento;
    @Schema(description = "Peso de la mascota en kg", example = "12.5")
    private BigDecimal peso;
    @Schema(description = "Temperatura corporal en grados Celsius", example = "38.5")
    private BigDecimal temperatura;
    @Schema(description = "Frecuencia cardíaca en latidos por minuto", example = "110")
    private Integer frecuenciaCardiaca;
    @Schema(description = "Frecuencia respiratoria en respiraciones por minuto", example = "28")
    private Integer frecuenciaRespiratoria;
    @Schema(description = "Tiempo de relleno capilar (TRC) en segundos", example = "2")
    private String trc;
    @Schema(description = "Notas adicionales")
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
