package com.team4.petstore.controller;

import com.team4.petstore.entity.Notificacion;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.service.NotificacionService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notificaciones")
@Tag(name = "Notificaciones", description = "Gestión de notificaciones del usuario")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final UsuarioRepository usuarioRepository;

    public NotificacionController(NotificacionService notificacionService, UsuarioRepository usuarioRepository) {
        this.notificacionService = notificacionService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Listar notificaciones", description = "Devuelve todas las notificaciones del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificaciones obtenidas correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificacionResponse>> listarNotificaciones(Authentication authentication) {
        Long usuarioId = getUsuarioId(authentication);
        List<Notificacion> notificaciones = notificacionService.listarPorUsuario(usuarioId);
        List<NotificacionResponse> response = notificaciones.stream()
            .map(this::mapToResponse)
            .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar notificaciones no leídas", description = "Devuelve solo las notificaciones no leídas del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificaciones obtenidas correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/no-leidas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificacionResponse>> listarNoLeidas(Authentication authentication) {
        Long usuarioId = getUsuarioId(authentication);
        List<Notificacion> notificaciones = notificacionService.listarNoLeidasPorUsuario(usuarioId);
        List<NotificacionResponse> response = notificaciones.stream()
            .map(this::mapToResponse)
            .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cantidad de notificaciones sin leer", description = "Devuelve la cantidad de notificaciones no leídas del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cantidad obtenida correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping("/sin-leer/cantidad")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Long>> cantidadSinLeer(Authentication authentication) {
        Long usuarioId = getUsuarioId(authentication);
        Long cantidad = notificacionService.cantidadSinLeer(usuarioId);
        Map<String, Long> response = new HashMap<>();
        response.put("cantidad", cantidad);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Marcar notificación como leída", description = "Marca una notificación específica como leída")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación marcada como leída"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PatchMapping("/{id}/leer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificacionResponse> marcarLeido(
            @Parameter(description = "ID de la notificación", required = true, example = "1")
            @PathVariable Long id) {
        Notificacion notificacion = notificacionService.marcarLeido(id);
        return ResponseEntity.ok(mapToResponse(notificacion));
    }

    @Operation(summary = "Marcar todas como leídas", description = "Marca todas las notificaciones del usuario como leídas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Todas las notificaciones marcadas como leídas"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PatchMapping("/marcar-todas-leidas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> marcarTodasLeidas(Authentication authentication) {
        Long usuarioId = getUsuarioId(authentication);
        notificacionService.marcarTodasLeidas(usuarioId);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Todas las notificaciones marcadas como leídas");
        return ResponseEntity.ok(response);
    }

    private Long getUsuarioId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getId();
    }

    private NotificacionResponse mapToResponse(Notificacion notificacion) {
        return new NotificacionResponse(
            notificacion.getId(),
            notificacion.getTitulo(),
            notificacion.getMensaje(),
            notificacion.getTipo().name(),
            notificacion.getLeido(),
            notificacion.getFechaCreacion()
        );
    }

    public static class NotificacionResponse {
        private Long id;
        private String titulo;
        private String mensaje;
        private String tipo;
        private Boolean leido;
        private java.time.LocalDateTime fechaCreacion;

        public NotificacionResponse(Long id, String titulo, String mensaje, String tipo, Boolean leido, java.time.LocalDateTime fechaCreacion) {
            this.id = id;
            this.titulo = titulo;
            this.mensaje = mensaje;
            this.tipo = tipo;
            this.leido = leido;
            this.fechaCreacion = fechaCreacion;
        }

        public Long getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getMensaje() { return mensaje; }
        public String getTipo() { return tipo; }
        public Boolean getLeido() { return leido; }
        public java.time.LocalDateTime getFechaCreacion() { return fechaCreacion; }
    }
}
