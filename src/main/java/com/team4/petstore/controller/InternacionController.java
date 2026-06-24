package com.team4.petstore.controller;

import com.team4.petstore.dto.request.AltaRequest;
import com.team4.petstore.dto.request.EvolucionRequest;
import com.team4.petstore.dto.request.InternacionRequest;
import com.team4.petstore.dto.request.ReingresoRequest;
import com.team4.petstore.dto.response.EvolucionResponse;
import com.team4.petstore.dto.response.InternacionResponse;
import com.team4.petstore.service.InternacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Internaciones", description = "Gestión de internaciones veterinarias: ingreso, evoluciones, alta y reingreso")
public class InternacionController {

    private final InternacionService internacionService;

    public InternacionController(InternacionService internacionService) {
        this.internacionService = internacionService;
    }

    @Operation(summary = "Ingresar mascota", description = "Registra una nueva internación. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Internación registrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Mascota o veterinario no encontrado")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<InternacionResponse> ingresar(
            @Valid @RequestBody InternacionRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(internacionService.ingresar(dto));
    }

    @Operation(summary = "Listar internaciones activas", description = "Devuelve las internaciones en estado ACTIVA. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de internaciones activas")
    })
    @GetMapping("/activas")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<List<InternacionResponse>> listarActivas() {
        return ResponseEntity.ok(internacionService.listarActivas());
    }

    @Operation(summary = "Registrar evolución clínica", description = "Agrega una entrada de evolución con observación, peso y temperatura a una internación activa. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Evolución registrada"),
        @ApiResponse(responseCode = "404", description = "Internación no encontrada")
    })
    @PostMapping("/{id}/evolucion")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<EvolucionResponse> registrarEvolucion(
            @Parameter(description = "ID de la internación") @PathVariable Long id,
            @Valid @RequestBody EvolucionRequest dto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(internacionService.registrarEvolucion(id, dto, userDetails.getUsername()));
    }

    @Operation(summary = "Dar de alta", description = "Cierra la internación. Acepta body opcional con indicacionesAlta. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Alta registrada"),
        @ApiResponse(responseCode = "400", description = "La internación no está activa"),
        @ApiResponse(responseCode = "404", description = "Internación no encontrada")
    })
    @PatchMapping("/{id}/alta")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<InternacionResponse> darDeAlta(
            @Parameter(description = "ID de la internación") @PathVariable Long id,
            @RequestBody(required = false) AltaRequest dto) {
        String indicaciones = dto != null ? dto.getIndicacionesAlta() : null;
        return ResponseEntity.ok(internacionService.darDeAlta(id, indicaciones));
    }

    @Operation(summary = "Listar internaciones pendientes de reingreso", description = "Devuelve internaciones dadas de alta que tienen un reingreso solicitado. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de pendientes de reingreso")
    })
    @GetMapping("/pendientes-reingreso")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<List<InternacionResponse>> listarPendientesReingreso() {
        return ResponseEntity.ok(internacionService.listarPendientesReingreso());
    }

    @Operation(summary = "Internaciones por mascota", description = "Lista todas las internaciones de una mascota específica. Cualquier usuario autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de internaciones de la mascota")
    })
    @GetMapping("/mascota/{mascotaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<InternacionResponse>> obtenerPorMascota(@Parameter(description = "ID de la mascota") @PathVariable Long mascotaId) {
        return ResponseEntity.ok(internacionService.obtenerPorMascota(mascotaId));
    }

    @Operation(summary = "Solicitar reingreso", description = "Crea una solicitud de reingreso para una mascota dada de alta. Cualquier usuario autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Solicitud de reingreso creada"),
        @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @PostMapping("/solicitar-reingreso")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<InternacionResponse> solicitarReingreso(
            @Valid @RequestBody ReingresoRequest dto,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(internacionService.solicitarReingreso(dto, userDetails.getUsername()));
    }

    @Operation(summary = "Confirmar reingreso", description = "Confirma una solicitud de reingreso y asigna un jaulaId. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reingreso confirmado"),
        @ApiResponse(responseCode = "400", description = "La internación no tiene reingreso solicitado"),
        @ApiResponse(responseCode = "404", description = "Internación no encontrada")
    })
    @PatchMapping("/{id}/confirmar-reingreso")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<InternacionResponse> confirmarReingreso(
            @Parameter(description = "ID de la internación") @PathVariable Long id,
            @Parameter(description = "ID de la jaula asignada", example = "J-12") @RequestParam("jaulaId") String jaulaId) {
        return ResponseEntity.ok(internacionService.confirmarReingreso(id, jaulaId));
    }
}
