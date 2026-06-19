package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Datos para registrar o actualizar un veterinario")
public class VeterinarioRequest {

    @Schema(description = "ID del usuario asociado", example = "10")
    private Long usuarioId;

    @Schema(description = "Matrícula profesional", example = "MAT-12345")
    @NotBlank
    private String matricula;

    @Schema(description = "Especialidad del veterinario", example = "Cirugía")
    private String especialidad;

    @Schema(description = "Biografía o descripción", example = "Especialista en cirugía de pequeños animales")
    private String bio;

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}
