package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ConsultaResponse {

    @Schema(description = "ID de la consulta", example = "1")
    private Long id;
    @Schema(description = "ID de la cita asociada (si aplica)", example = "1")
    private Long citaId;
    @Schema(description = "ID de la mascota", example = "1")
    private Long mascotaId;
    @Schema(description = "Nombre de la mascota", example = "Max")
    private String mascotaNombre;
    @Schema(description = "ID del veterinario", example = "3")
    private Long veterinarioId;
    @Schema(description = "Nombre del veterinario", example = "Juan Perez")
    private String veterinarioNombre;
    @Schema(description = "URL del avatar del veterinario")
    private String veterinarioAvatar;
    @Schema(description = "Motivo de la consulta")
    private String motivo;
    @Schema(description = "Anamnesis registrada")
    private String anamnesis;
    @Schema(description = "Examen físico registrado")
    private String examenFisico;
    @Schema(description = "Diagnóstico")
    private String diagnostico;
    @Schema(description = "Tratamiento indicado")
    private String tratamiento;
    @Schema(description = "Peso de la mascota en kg")
    private BigDecimal peso;
    @Schema(description = "Temperatura corporal en °C")
    private BigDecimal temperatura;
    @Schema(description = "Frecuencia cardíaca (lpm)")
    private Integer frecuenciaCardiaca;
    @Schema(description = "Frecuencia respiratoria (rpm)")
    private Integer frecuenciaRespiratoria;
    @Schema(description = "Tiempo de relleno capilar")
    private String trc;
    @Schema(description = "Notas adicionales")
    private String notas;
    @Schema(description = "Fecha y hora de creación de la consulta")
    private LocalDateTime fechaCreacion;

    public ConsultaResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    public Long getMascotaId() { return mascotaId; }
    public void setMascotaId(Long mascotaId) { this.mascotaId = mascotaId; }
    public String getMascotaNombre() { return mascotaNombre; }
    public void setMascotaNombre(String mascotaNombre) { this.mascotaNombre = mascotaNombre; }
    public Long getVeterinarioId() { return veterinarioId; }
    public void setVeterinarioId(Long veterinarioId) { this.veterinarioId = veterinarioId; }
    public String getVeterinarioNombre() { return veterinarioNombre; }
    public void setVeterinarioNombre(String veterinarioNombre) { this.veterinarioNombre = veterinarioNombre; }
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
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getVeterinarioAvatar() { return veterinarioAvatar; }
    public void setVeterinarioAvatar(String veterinarioAvatar) { this.veterinarioAvatar = veterinarioAvatar; }
}
