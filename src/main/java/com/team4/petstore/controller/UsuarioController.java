package com.team4.petstore.controller;

import com.team4.petstore.entity.Usuario;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuario", description = "Gestión de perfil de usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Actualizar avatar", description = "Permite al usuario actualizar su propia foto de perfil")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avatar actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Archivo inválido o demasiado grande"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/avatar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> updateAvatar(
            @Parameter(description = "Archivo de imagen (máx 5MB)", required = true)
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {
        Long usuarioId = getUsuarioId(authentication);
        String avatarUrl = usuarioService.updateAvatar(usuarioId, file);

        Map<String, String> response = new HashMap<>();
        response.put("url", avatarUrl);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener perfil", description = "Devuelve los datos del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil obtenido correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/perfil")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioPerfilResponse> getPerfil(Authentication authentication) {
        Long usuarioId = getUsuarioId(authentication);
        Usuario usuario = usuarioService.getById(usuarioId);

        UsuarioPerfilResponse response = new UsuarioPerfilResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getEmail(),
            usuario.getAvatar(),
            usuario.getDireccion(),
            usuario.getCelular()
        );
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar avatar de usuario (ADMIN)", description = "Permite al ADMIN actualizar la foto de perfil de cualquier usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Avatar actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Archivo inválido o demasiado grande"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - solo ADMIN"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @PutMapping("/admin/{id}/avatar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> updateAvatarAdmin(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Archivo de imagen (máx 5MB)", required = true)
            @RequestParam("file") MultipartFile file) throws IOException {
        String avatarUrl = usuarioService.updateAvatar(id, file);

        Map<String, String> response = new HashMap<>();
        response.put("url", avatarUrl);
        return ResponseEntity.ok(response);
    }

    private Long getUsuarioId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getId();
    }

    public static class UsuarioPerfilResponse {
        private Long id;
        private String nombre;
        private String apellido;
        private String email;
        private String avatar;
        private String direccion;
        private String celular;

        public UsuarioPerfilResponse(Long id, String nombre, String apellido, String email,
                                    String avatar, String direccion, String celular) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.email = email;
            this.avatar = avatar;
            this.direccion = direccion;
            this.celular = celular;
        }

        public Long getId() { return id; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getEmail() { return email; }
        public String getAvatar() { return avatar; }
        public String getDireccion() { return direccion; }
        public String getCelular() { return celular; }
    }
}
