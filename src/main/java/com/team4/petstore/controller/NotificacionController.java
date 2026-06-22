package com.team4.petstore.controller;

import com.team4.petstore.dto.response.NotificacionResponse;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.service.NotificacionService;
import com.team4.petstore.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;
    private final UsuarioRepository usuarioRepository;

    public NotificacionController(NotificacionService notificacionService, UsuarioRepository usuarioRepository) {
        this.notificacionService = notificacionService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'CLIENTE')")
    public ResponseEntity<List<NotificacionResponse>> listarMisNotificaciones(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(notificacionService.listarPorUsuario(usuario.getId()));
    }

    @GetMapping("/sin-leer/cantidad")
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'CLIENTE')")
    public ResponseEntity<Map<String, Long>> cantidadSinLeer(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        long count = notificacionService.cantidadSinLeer(usuario.getId());
        return ResponseEntity.ok(Map.of("cantidad", count));
    }

    @PatchMapping("/{id}/leer")
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'CLIENTE')")
    public ResponseEntity<NotificacionResponse> marcarLeido(@PathVariable Long id) {
        return ResponseEntity.ok(notificacionService.marcarLeido(id));
    }
}
