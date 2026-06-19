package com.team4.petstore.controller;

import com.team4.petstore.dto.request.EvolucionRequest;
import com.team4.petstore.dto.request.InternacionRequest;
import com.team4.petstore.dto.response.EvolucionResponse;
import com.team4.petstore.dto.response.InternacionResponse;
import com.team4.petstore.service.InternacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_VETERINARIO')")
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
    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_VETERINARIO')")
    public ResponseEntity<InternacionResponse> darDeAlta(@PathVariable Long id) {
        return ResponseEntity.ok(internacionService.darDeAlta(id));
    }
}
