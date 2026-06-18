package com.team4.petstore.dto.response;

public class AuthResponse {
    private Long id;
    private String nombre;
    private String apellido;
    private String direccion;
    private String celular;
    private String email;
    private String token;

    public AuthResponse() {}

    public AuthResponse(Long id, String nombre, String apellido, String email, String token) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.token = token;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
