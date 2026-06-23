package com.team4.petstore.controller;

import com.team4.petstore.dto.request.BloqueoFechaRequest;
import com.team4.petstore.dto.request.CambiarEstadoRequest;
import com.team4.petstore.dto.request.HorarioRequest;
import com.team4.petstore.dto.request.VeterinarioRequest;
import com.team4.petstore.dto.response.BloqueoFechaResponse;
import com.team4.petstore.dto.response.HorarioResponse;
import com.team4.petstore.dto.response.VeterinarioResponse;
import com.team4.petstore.service.VeterinarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/veterinarios")
@Tag(name = "Veterinarios", description = "Gestión de veterinarios (ADMIN)")
public class VeterinarioController {

    private final VeterinarioService veterinarioService;

    public VeterinarioController(VeterinarioService veterinarioService) {
        this.veterinarioService = veterinarioService;
    }

    @Operation(summary = "Listar veterinarios", description = "Obtiene todos los veterinarios activos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de veterinarios obtenida correctamente")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VeterinarioResponse>> listar() {
        return ResponseEntity.ok(veterinarioService.listar());
    }

    @Operation(summary = "Listar todos los veterinarios", description = "Obtiene todos los veterinarios incluyendo inactivos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de veterinarios obtenida correctamente")
    })
    @GetMapping("/todos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VeterinarioResponse>> listarTodos() {
        return ResponseEntity.ok(veterinarioService.listarTodos());
    }

    @Operation(summary = "Obtener veterinario por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Veterinario encontrado"),
        @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VeterinarioResponse> obtenerPorId(
            @Parameter(description = "ID del veterinario") @PathVariable Long id) {
        return ResponseEntity.ok(veterinarioService.obtenerPorId(id));
    }

    @Operation(summary = "Crear veterinario", description = "Crea un nuevo veterinario con usuario y credenciales (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Veterinario creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o email/matrícula duplicada"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - solo ADMIN")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VeterinarioResponse> crear(@Valid @RequestBody VeterinarioRequest request) {
        VeterinarioResponse response = veterinarioService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar veterinario", description = "Actualiza un veterinario existente (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Veterinario actualizado"),
        @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VeterinarioResponse> actualizar(
            @Parameter(description = "ID del veterinario") @PathVariable Long id,
            @Valid @RequestBody VeterinarioRequest request) {
        return ResponseEntity.ok(veterinarioService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar veterinario", description = "Elimina un veterinario (soft delete, solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Veterinario eliminado"),
        @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del veterinario") @PathVariable Long id) {
        veterinarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cambiar estado del veterinario", description = "Activa o desactiva un veterinario (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado"),
        @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @PatchMapping("/{id}/activo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VeterinarioResponse> cambiarEstado(
            @Parameter(description = "ID del veterinario") @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoRequest request) {
        return ResponseEntity.ok(veterinarioService.cambiarEstado(id, request.getActivo()));
    }

    @Operation(summary = "Listar horarios del veterinario", description = "Obtiene los horarios de atención de un veterinario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horarios obtenidos correctamente"),
        @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @GetMapping("/{id}/horarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HorarioResponse>> listarHorarios(
            @Parameter(description = "ID del veterinario") @PathVariable Long id) {
        return ResponseEntity.ok(veterinarioService.listarHorarios(id));
    }

    @Operation(summary = "Actualizar horarios del veterinario", description = "Actualiza todos los horarios de atención (7 días)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Horarios actualizados"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @PutMapping("/{id}/horarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HorarioResponse>> actualizarHorarios(
            @Parameter(description = "ID del veterinario") @PathVariable Long id,
            @Valid @RequestBody HorarioRequest request) {
        return ResponseEntity.ok(veterinarioService.actualizarHorarios(id, request));
    }

    @Operation(summary = "Listar bloqueos del veterinario", description = "Obtiene los bloqueos de fechas (vacaciones/ausencias)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bloqueos obtenidos correctamente"),
        @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @GetMapping("/{id}/bloqueos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BloqueoFechaResponse>> listarBloqueos(
            @Parameter(description = "ID del veterinario") @PathVariable Long id) {
        return ResponseEntity.ok(veterinarioService.listarBloqueos(id));
    }

    @Operation(summary = "Crear bloqueo de fechas", description = "Registra un período de ausencia/vacaciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Bloqueo creado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o fechas superpuestas"),
        @ApiResponse(responseCode = "404", description = "Veterinario no encontrado")
    })
    @PostMapping("/{id}/bloqueos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BloqueoFechaResponse> crearBloqueo(
            @Parameter(description = "ID del veterinario") @PathVariable Long id,
            @Valid @RequestBody BloqueoFechaRequest request) {
        BloqueoFechaResponse response = veterinarioService.crearBloqueo(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Eliminar bloqueo de fechas", description = "Elimina un bloqueo de fecha")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Bloqueo eliminado"),
        @ApiResponse(responseCode = "404", description = "Veterinario o bloqueo no encontrado")
    })
    @DeleteMapping("/{id}/bloqueos/{bloqueoId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarBloqueo(
            @Parameter(description = "ID del veterinario") @PathVariable Long id,
            @Parameter(description = "ID del bloqueo") @PathVariable Long bloqueoId) {
        veterinarioService.eliminarBloqueo(id, bloqueoId);
        return ResponseEntity.noContent().build();
    }
}
