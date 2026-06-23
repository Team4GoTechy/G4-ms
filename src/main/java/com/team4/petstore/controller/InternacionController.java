package com.team4.petstore.controller;

import com.team4.petstore.dto.request.AltaRequest;
import com.team4.petstore.dto.request.EvolucionRequest;
import com.team4.petstore.dto.request.InternacionRequest;
import com.team4.petstore.dto.request.ReingresoRequest;
import com.team4.petstore.dto.response.EvolucionResponse;
import com.team4.petstore.dto.response.InternacionResponse;
import com.team4.petstore.service.InternacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internaciones")
public class InternacionController {

    private final InternacionService internacionService;

    public InternacionController(InternacionService internacionService) {
        this.internacionService = internacionService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<InternacionResponse> ingresar(
            @Valid @RequestBody InternacionRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(internacionService.ingresar(dto));
    }

    @GetMapping("/activas")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<List<InternacionResponse>> listarActivas() {
        return ResponseEntity.ok(internacionService.listarActivas());
    }

    @PostMapping("/{id}/evolucion")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<EvolucionResponse> registrarEvolucion(
            @PathVariable Long id,
            @Valid @RequestBody EvolucionRequest dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(internacionService.registrarEvolucion(id, dto, userDetails.getUsername()));
    }

    @PatchMapping("/{id}/alta")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<InternacionResponse> darDeAlta(
            @PathVariable Long id,
            @RequestBody(required = false) AltaRequest dto) {
        String indicaciones = dto != null ? dto.getIndicacionesAlta() : null;
        return ResponseEntity.ok(internacionService.darDeAlta(id, indicaciones));
    }

    @GetMapping("/pendientes-reingreso")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<List<InternacionResponse>> listarPendientesReingreso() {
        return ResponseEntity.ok(internacionService.listarPendientesReingreso());
    }

    @GetMapping("/mascota/{mascotaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<InternacionResponse>> obtenerPorMascota(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(internacionService.obtenerPorMascota(mascotaId));
    }

    @PostMapping("/solicitar-reingreso")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InternacionResponse> solicitarReingreso(
            @Valid @RequestBody ReingresoRequest dto,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(internacionService.solicitarReingreso(dto, userDetails.getUsername()));
    }

    @PatchMapping("/{id}/confirmar-reingreso")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<InternacionResponse> confirmarReingreso(
            @PathVariable Long id,
            @RequestParam("jaulaId") String jaulaId) {
        return ResponseEntity.ok(internacionService.confirmarReingreso(id, jaulaId));
    }
}
