package com.team4.petstore.controller;

import com.team4.petstore.dto.response.ConsultaResponse;
import com.team4.petstore.service.ConsultaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mascotas")
public class MascotaHistorialController {

    private final ConsultaService consultaService;

    public MascotaHistorialController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping("/{id}/historial-clinico")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ROLE_CLIENT', 'ROLE_CLIENTE', 'ADMIN', 'VETERINARIO', 'DOCTOR', 'CLIENT', 'CLIENTE')")
    public ResponseEntity<Page<ConsultaResponse>> historial(
            @PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(consultaService.historialPorMascota(id, pageable));
    }
}
