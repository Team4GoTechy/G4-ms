package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;

@Schema(description = "Actualización de horarios de atención del veterinario")
public class HorarioRequest {

    @Schema(description = "Lista de horarios por día de la semana (1=Lunes a 7=Domingo)")
    @NotNull(message = "La lista de horarios no puede ser nula")
    private List<HorarioDiaRequest> horarios;

    public HorarioRequest() {}

    public List<HorarioDiaRequest> getHorarios() { return horarios; }
    public void setHorarios(List<HorarioDiaRequest> horarios) { this.horarios = horarios; }

    @Schema(description = "Horario de un día específico")
    public static class HorarioDiaRequest {

        @Schema(description = "Día de la semana (1=Lunes, 7=Domingo)", example = "1")
        @NotNull(message = "El día de la semana es obligatorio")
        private Integer diaSemana;

        @Schema(description = "Indica si trabaja ese día", example = "true")
        @NotNull(message = "Debe especificar si trabaja este día")
        private Boolean trabaja;

        @Schema(description = "Hora de inicio (requerido si trabaja=true)", example = "08:00")
        private LocalTime horaInicio;

        @Schema(description = "Hora de fin (requerido si trabaja=true)", example = "12:00")
        private LocalTime horaFin;

        public HorarioDiaRequest() {}

        public Integer getDiaSemana() { return diaSemana; }
        public void setDiaSemana(Integer diaSemana) { this.diaSemana = diaSemana; }

        public Boolean getTrabaja() { return trabaja; }
        public void setTrabaja(Boolean trabaja) { this.trabaja = trabaja; }

        public LocalTime getHoraInicio() { return horaInicio; }
        public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

        public LocalTime getHoraFin() { return horaFin; }
        public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    }
}
