package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalTime;

@Schema(description = "Respuesta con datos del horario de atención")
public class HorarioResponse {

    @Schema(description = "ID del horario", example = "1")
    private Long id;

    @Schema(description = "Día de la semana (1=Lunes, 7=Domingo)", example = "1")
    private Integer diaSemana;

    @Schema(description = "Nombre del día", example = "Lunes")
    private String nombreDia;

    @Schema(description = "Hora de inicio", example = "08:00")
    private LocalTime horaInicio;

    @Schema(description = "Hora de fin", example = "12:00")
    private LocalTime horaFin;

    @Schema(description = "Indica si trabaja este día", example = "true")
    private Boolean trabaja;

    public HorarioResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getDiaSemana() { return diaSemana; }
    public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }

    public String getNombreDia() { return nombreDia; }
    public void setNombreDia(String nombreDia) { this.nombreDia = nombreDia; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public Boolean getTrabaja() { return trabaja; }
    public void setTrabaja(Boolean trabaja) { this.trabaja = trabaja; }
}
