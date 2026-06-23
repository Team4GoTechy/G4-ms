package com.team4.petstore.controller;

import com.team4.petstore.dto.request.CitaRequest;
import com.team4.petstore.dto.request.EstadoCitaRequest;
import com.team4.petstore.dto.response.CitaResponse;
import com.team4.petstore.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.exception.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/v1/citas")
public class CitaController {

    private final CitaService citaService;
    private final UsuarioRepository usuarioRepository;

    public CitaController(CitaService citaService, UsuarioRepository usuarioRepository) {
        this.citaService = citaService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ROLE_CLIENTE', 'ADMIN', 'VETERINARIO', 'DOCTOR', 'CLIENTE')")
    public ResponseEntity<CitaResponse> crear(@Valid @RequestBody CitaRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.crear(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<CitaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.buscarPorId(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ROLE_CLIENTE', 'ADMIN', 'VETERINARIO', 'DOCTOR', 'CLIENTE')")
    public ResponseEntity<List<CitaResponse>> obtenerAgenda(
            @RequestParam Long veterinarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(citaService.obtenerAgendaDelDia(veterinarioId, fecha));
    }
    
    @GetMapping("/agenda/mes")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<List<CitaResponse>> obtenerAgendaDelMes(
            @RequestParam Long veterinarioId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
        return ResponseEntity.ok(citaService.obtenerAgendaDelMes(veterinarioId, mes));
    }

    @GetMapping("/todas")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<List<CitaResponse>> obtenerTodasLasCitas(
            @RequestParam Long veterinarioId) {
        return ResponseEntity.ok(citaService.obtenerTodasLasCitas(veterinarioId));
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<CitaResponse> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody EstadoCitaRequest dto) {
        return ResponseEntity.ok(citaService.actualizarEstado(id, dto));
    }

    @GetMapping("/mis-citas")
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'CLIENTE')")
    public ResponseEntity<List<CitaResponse>> obtenerMisCitas(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(citaService.obtenerCitasCliente(usuario.getId()));
    }

    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'CLIENTE')")
    public ResponseEntity<CitaResponse> pagarCita(@PathVariable Long id) {
        return ResponseEntity.ok(citaService.pagarCita(id));
    }
}
