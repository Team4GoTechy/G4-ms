package com.team4.petstore.dto.request;

import com.team4.petstore.entity.enums.TipoCita;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Schema(description = "Perfil profesional del veterinario (Paso 2)")
public class VeterinarioPerfilRequest {

    @Schema(description = "Número de matrícula profesional (debe ser único)", example = "VET-2024-001")
    @NotBlank(message = "El número de matrícula es obligatorio")
    @Size(max = 50, message = "La matrícula no puede exceder 50 caracteres")
    private String matricula;

    @Schema(description = "Especialidad del veterinario", example = "Cirugía General")
    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 100, message = "La especialidad no puede exceder 100 caracteres")
    private String especialidad;

    @Schema(description = "Biografía presentacion del veterinario (opcional)", example = "Médico veterinario con más de 10 años de experiencia en...")
    private String bio;

    @Schema(description = "Servicios habilitados para este veterinario", example = "[\"CONSULTA\", \"VACUNACION\", \"CIRUGIA\"]")
    @NotEmpty(message = "Debe seleccionar al menos un servicio")
    private Set<TipoCita> serviciosHabilitados;

    public VeterinarioPerfilRequest() {}

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Set<TipoCita> getServiciosHabilitados() { return serviciosHabilitados; }
    public void setServiciosHabilitados(Set<TipoCita> serviciosHabilitados) { this.serviciosHabilitados = serviciosHabilitados; }
}
