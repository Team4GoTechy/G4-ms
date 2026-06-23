package com.team4.petstore.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ServicioRequest {
    @NotBlank
    private String nombre;
    private String descripcion;
    @NotNull
    private Double precio;
    private List<Long> veterinarioIds;
    
    public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
    
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	public List<Long> getVeterinarioIds() {
		return veterinarioIds;
	}
	public void setVeterinarioIds(List<Long> veterinarioIds) {
		this.veterinarioIds = veterinarioIds;
	}
}