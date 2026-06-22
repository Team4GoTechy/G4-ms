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

    @Schema(description = "Raza de la mascota", example = "Caniche")
    private String raza;

    @Schema(description = "Fecha de nacimiento", example = "2024-10-12")
    private String fechaNacimiento;

    @Schema(description = "Peso de la mascota en kg", example = "4.5")
    private Double peso;

    public MascotaResponse() {}

    public MascotaResponse(Long id, String nombre, String sexo, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.sexo = sexo;
        this.tipo = tipo;
    }

    public MascotaResponse(Long id, String nombre, String sexo, String tipo, String raza, String fechaNacimiento, Double peso) {
        this.id = id;
        this.nombre = nombre;
        this.sexo = sexo;
        this.tipo = tipo;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.peso = peso;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
}
