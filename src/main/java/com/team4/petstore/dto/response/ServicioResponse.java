package com.team4.petstore.dto.response;

import java.util.List;

public class ServicioResponse {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private List<VeterinarioResponse> veterinarios;

    public ServicioResponse(Long id, String nombre, String descripcion, Double precio,
                            List<VeterinarioResponse> veterinarios) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.setVeterinarios(veterinarios);
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public List<VeterinarioResponse> getVeterinarios() {
		return veterinarios;
	}

	public void setVeterinarios(List<VeterinarioResponse> veterinarios) {
		this.veterinarios = veterinarios;
	}

}
