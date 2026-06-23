package com.team4.petstore.dto.response;

import com.team4.petstore.entity.enums.TipoCita;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Set;

@Schema(description = "Respuesta con datos del veterinario")
public class VeterinarioResponse {

    @Schema(description = "ID del veterinario", example = "1")
    private Long id;

    @Schema(description = "ID del usuario asociado", example = "5")
    private Long usuarioId;

    @Schema(description = "Nombre completo", example = "Juan Perez")
    private String nombreCompleto;

    @Schema(description = "URL del avatar", example = "https://res.cloudinary.com/...")
    private String avatar;

    @Schema(description = "Correo electrónico", example = "juan.perez@petstore.com")
    private String email;

    @Schema(description = "Teléfono de contacto", example = "351-555-1234")
    private String telefono;

    @Schema(description = "Número de matrícula", example = "VET-2024-001")
    private String matricula;

    @Schema(description = "Especialidad", example = "Cirugía General")
    private String especialidad;

    @Schema(description = "Biografía del veterinario", example = "Médico veterinario con más de 10 años de experiencia...")
    private String bio;

    @Schema(description = "Indica si está activo", example = "true")
    private Boolean activo;

    @Schema(description = "Servicios habilitados", example = "[\"CONSULTA\", \"VACUNACION\", \"CIRUGIA\"]")
    private Set<TipoCita> serviciosHabilitados;

    @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
    private LocalDateTime fechaCreacion;

    public VeterinarioResponse() {}

    public VeterinarioResponse(Long id, Long usuarioId, String nombreCompleto, String matricula,
                               String especialidad, String bio, Boolean activo, String avatar) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombreCompleto = nombreCompleto;
        this.matricula = matricula;
        this.especialidad = especialidad;
        this.bio = bio;
        this.activo = activo;
        this.avatar = avatar;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Set<TipoCita> getServiciosHabilitados() { return serviciosHabilitados; }
    public void setServiciosHabilitados(Set<TipoCita> serviciosHabilitados) { this.serviciosHabilitados = serviciosHabilitados; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
