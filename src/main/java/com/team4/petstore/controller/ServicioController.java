package com.team4.petstore.controller;

import com.team4.petstore.dto.request.ServicioRequest;
import com.team4.petstore.dto.response.ServicioResponse;
import com.team4.petstore.dto.response.VeterinarioResponse;
import com.team4.petstore.service.ServicioService;
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
@RequestMapping("/api/v1/servicios")
@Tag(name = "Servicios", description = "Catálogo de servicios veterinarios y asignación a veterinarios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @Operation(summary = "Crear servicio", description = "Crea un servicio en el catálogo. Roles: ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Servicio creado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicioResponse> crear(@Valid @RequestBody ServicioRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioService.crear(dto));
    }

    @Operation(summary = "Listar todos los veterinarios", description = "Devuelve todos los veterinarios del sistema. Usado para asignar servicios. Roles: ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de veterinarios")
    })
    @GetMapping("/veterinarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VeterinarioResponse>> obtenerTodosLosVeterinarios() {
        return ResponseEntity.ok(servicioService.obtenerTodosLosVeterinarios());
    }

    @Operation(summary = "Listar todos los servicios", description = "Lista completa del catálogo de servicios. Roles: ADMIN, VETERINARIO, CLIENTE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de servicios")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ROLE_CLIENTE', 'ADMIN', 'VETERINARIO', 'DOCTOR', 'CLIENTE')")
    public ResponseEntity<List<ServicioResponse>> listarTodos() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    @Operation(summary = "Actualizar servicio", description = "Actualiza un servicio existente. Roles: ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio actualizado"),
        @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicioResponse> actualizar(@Parameter(description = "ID del servicio") @PathVariable Long id, @Valid @RequestBody ServicioRequest dto) {
        return ResponseEntity.ok(servicioService.actualizar(id, dto));
    }

    @Operation(summary = "Servicios de un veterinario", description = "Lista los servicios ofrecidos por un veterinario específico. Roles: ADMIN, VETERINARIO, CLIENTE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicios del veterinario")
    })
    @GetMapping("/veterinario/{veterinarioId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ROLE_CLIENTE', 'ADMIN', 'VETERINARIO', 'DOCTOR', 'CLIENTE')")
    public ResponseEntity<List<ServicioResponse>> listarPorVeterinario(@Parameter(description = "ID del veterinario") @PathVariable Long veterinarioId) {
        return ResponseEntity.ok(servicioService.listarPorVeterinario(veterinarioId));
    }

    @Operation(summary = "Eliminar servicio", description = "Elimina un servicio del catálogo. Roles: ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Servicio eliminado"),
        @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@Parameter(description = "ID del servicio") @PathVariable Long id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
