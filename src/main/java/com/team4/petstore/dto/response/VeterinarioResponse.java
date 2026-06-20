package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos devueltos de un veterinario")
public class VeterinarioResponse {

    private Long id;
    private String nombreUsuario;
    private String matricula;
    private String especialidad;
    private String bio;
    private Boolean activo;

    public VeterinarioResponse() {}

    public VeterinarioResponse(Long id, String nombreUsuario, String matricula,
                               String especialidad, String bio, Boolean activo) {
        this.setId(id);
        this.setNombreUsuario(nombreUsuario);
        this.setMatricula(matricula);
        this.setEspecialidad(especialidad);
        this.setBio(bio);
        this.setActivo(activo);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}
