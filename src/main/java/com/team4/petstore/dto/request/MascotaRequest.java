package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Datos de la mascota a registrar")
public class MascotaRequest {

    @Schema(description = "Nombre de la mascota", example = "Firulais")
    @NotBlank(message = "El nombre de la mascota es obligatorio")
    private String nombre;

    @Schema(description = "Sexo de la mascota", example = "Macho", allowableValues = {"Macho", "Hembra"})
    @NotBlank(message = "El sexo es obligatorio")
    @Pattern(regexp = "^(Macho|Hembra)$", message = "El sexo debe ser 'Macho' o 'Hembra'")
    private String sexo;

    @Schema(description = "Tipo de mascota", example = "Perro", allowableValues = {"Gato", "Perro"})
    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(regexp = "^(Gato|Perro)$", message = "El tipo debe ser 'Gato' o 'Perro'")
    private String tipo;

    public MascotaRequest() {}

    public MascotaRequest(String nombre, String sexo, String tipo) {
        this.nombre = nombre;
        this.sexo = sexo;
        this.tipo = tipo;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}
