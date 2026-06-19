package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de la mascota devueltos en las consultas")
public class MascotaResponse {

    @Schema(description = "ID único de la mascota", example = "1")
    private Long id;

    @Schema(description = "Nombre de la mascota", example = "Firulais")
    private String nombre;

    @Schema(description = "Sexo de la mascota", example = "Macho")
    private String sexo;

    @Schema(description = "Tipo de mascota", example = "Perro")
    private String tipo;

    public MascotaResponse() {}

    public MascotaResponse(Long id, String nombre, String sexo, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.sexo = sexo;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
