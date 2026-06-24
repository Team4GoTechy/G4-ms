package com.team4.petstore.controller;

import com.team4.petstore.dto.response.ConsultaResponse;
import com.team4.petstore.service.ConsultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mascotas")
@Tag(name = "Mascotas", description = "Gestión de mascotas: listado, actualización y prescripciones")
public class MascotaHistorialController {

    private final ConsultaService consultaService;

    public MascotaHistorialController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @Operation(summary = "Historial clínico de la mascota", description = "Devuelve el historial paginado de consultas de una mascota. Roles: ADMIN, VETERINARIO, CLIENTE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial paginado")
    })
    @GetMapping("/{id}/historial-clinico")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ROLE_CLIENT', 'ROLE_CLIENTE', 'ADMIN', 'VETERINARIO', 'DOCTOR', 'CLIENT', 'CLIENTE')")
    public ResponseEntity<Page<ConsultaResponse>> historial(
            @Parameter(description = "ID de la mascota") @PathVariable Long id,
            @Parameter(description = "Parámetros de paginación (page, size, sort)") Pageable pageable) {
        return ResponseEntity.ok(consultaService.historialPorMascota(id, pageable));
    }
}
