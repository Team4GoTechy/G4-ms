package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class NotificacionResponse {

    @Schema(description = "ID de la notificación", example = "1")
    private Long id;
    @Schema(description = "Título de la notificación", example = "Tu cita fue confirmada")
    private String titulo;
    @Schema(description = "Cuerpo del mensaje", example = "Tu cita del 2026-07-15 a las 10:00 fue confirmada por el veterinario")
    private String mensaje;
    @Schema(description = "Tipo de notificación", example = "SISTEMA", allowableValues = {"SISTEMA", "WHATSAPP", "EMAIL"})
    private String tipo;
    @Schema(description = "Indica si la notificación fue leída", example = "false")
    private Boolean leido;
    @Schema(description = "Fecha y hora de creación")
    private LocalDateTime fechaCreacion;

    public NotificacionResponse() {}

    public NotificacionResponse(Long id, String titulo, String mensaje, String tipo, Boolean leido, LocalDateTime fechaCreacion) {
        this.id = id;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.tipo = tipo;
        this.leido = leido;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Boolean getLeido() { return leido; }
    public void setLeido(Boolean leido) { this.leido = leido; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
