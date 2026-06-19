package com.team4.petstore.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de autenticacion con datos del usuario y token JWT")
public class AuthResponse {
    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre del usuario", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Perez")
    private String apellido;

    @Schema(description = "URL del avatar del usuario", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "Direccion del usuario", example = "Calle Principal 123")
    private String direccion;

    @Schema(description = "Celular del usuario", example = "+5491123456789")
    private String celular;

    @Schema(description = "Email del usuario", example = "juan.perez@email.com")
    private String email;

    @Schema(description = "Token JWT para autenticacion")
    private String token;

    @Schema(description = "Nombre de la primera mascota registrada", example = "Firulais")
    private String nombreMascota;

    @Schema(description = "Tipo de la primera mascota registrada", example = "Perro")
    private String tipoMascota;

    @Schema(description = "Cantidad total de mascotas registradas", example = "2")
    private Integer cantidadMascotas;

    public AuthResponse() {}

    public AuthResponse(Long id, String nombre, String apellido, String email, String token) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.token = token;
    }

    public AuthResponse(Long id, String nombre, String apellido, String direccion, String celular, String email, String token) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.celular = celular;
        this.email = email;
        this.token = token;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getNombreMascota() { return nombreMascota; }
    public void setNombreMascota(String nombreMascota) { this.nombreMascota = nombreMascota; }
    public String getTipoMascota() { return tipoMascota; }
    public void setTipoMascota(String tipoMascota) { this.tipoMascota = tipoMascota; }
    public Integer getCantidadMascotas() { return cantidadMascotas; }
    public void setCantidadMascotas(Integer cantidadMascotas) { this.cantidadMascotas = cantidadMascotas; }
}
