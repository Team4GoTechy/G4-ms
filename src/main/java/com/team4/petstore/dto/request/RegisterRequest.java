package com.team4.petstore.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(description = "Solicitud de registro de usuario con datos personales y mascotas")
public class RegisterRequest {

    @Schema(description = "Nombre del usuario", example = "Juan")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;

    @Schema(description = "Edad del usuario", example = "30")
    @Min(value = 1, message = "La edad debe ser mayor a 0")
    private Integer edad;

    @Schema(description = "URL de la imagen avatar del usuario", example = "https://example.com/avatar.jpg")
    private String avatar;

    @Schema(description = "Dirección del usuario", example = "Calle Principal 123")
    @Size(max = 255)
    private String direccion;

    @Schema(description = "Número de celular", example = "+5491123456789")
    @Size(max = 20)
    private String celular;

    @Schema(description = "Email del usuario", example = "juan.perez@email.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @Schema(description = "Contraseña", example = "password123")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "Cantidad de mascotas", example = "2")
    @Min(value = 0, message = "La cantidad de mascotas no puede ser negativa")
    private Integer cantidadMascotas = 0;

    @Schema(description = "Nombre de familia (requerido si cantidadMascotas >= 3)", example = "Familia Pérez")
    private String familyName;

    @Schema(description = "Lista de mascotas a registrar")
    private List<MascotaRequest> mascotas;

    public RegisterRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Integer getCantidadMascotas() { return cantidadMascotas; }
    public void setCantidadMascotas(Integer cantidadMascotas) { this.cantidadMascotas = cantidadMascotas; }
    public String getFamilyName() { return familyName; }
    public void setFamilyName(String familyName) { this.familyName = familyName; }
    public List<MascotaRequest> getMascotas() { return mascotas; }
    public void setMascotas(List<MascotaRequest> mascotas) { this.mascotas = mascotas; }
}
