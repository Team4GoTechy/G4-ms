package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Schema(description = "Solicitud para crear o actualizar un veterinario completo")
public class VeterinarioRequest {

    @Schema(description = "Nombre del veterinario", example = "Juan")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;

    @Schema(description = "Apellido del veterinario", example = "Perez")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    private String apellido;

    @Schema(description = "Correo electrónico (debe ser único)", example = "juan.perez@petstore.com")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico es inválido")
    private String email;

    @Schema(description = "Contraseña (obligatorio al crear)", example = "securePassword123")
    private String password;

    @Schema(description = "Teléfono de contacto (opcional)", example = "351-555-1234")
    private String telefono;

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

    @Schema(description = "IDs de los servicios que este veterinario puede ofrecer", example = "[1, 2, 3]")
    @NotEmpty(message = "Debe asignar al menos un servicio")
    private Set<Long> servicioIds;

    public VeterinarioRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Set<Long> getServicioIds() { return servicioIds; }
    public void setServicioIds(Set<Long> servicioIds) { this.servicioIds = servicioIds; }
}
