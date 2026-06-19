package com.team4.petstore.controller;

import com.team4.petstore.dto.request.InsumoRequest;
import com.team4.petstore.dto.response.InsumoResponse;
import com.team4.petstore.service.InsumoService;
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
@RequestMapping("/insumos")
@Tag(name = "Insumos", description = "Gestion de insumos medicos (ADMIN)")
public class InsumoController {

    private final InsumoService insumoService;

    public InsumoController(InsumoService insumoService) {
        this.insumoService = insumoService;
    }

    @Operation(summary = "Listar insumos", description = "Obtiene todos los insumos activos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de insumos obtenida correctamente")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InsumoResponse>> listar() {
        return ResponseEntity.ok(insumoService.listar());
    }

    @Operation(summary = "Obtener insumo por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Insumo encontrado"),
        @ApiResponse(responseCode = "404", description = "Insumo no encontrado")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InsumoResponse> obtenerPorId(
            @Parameter(description = "ID del insumo") @PathVariable Long id) {
        return ResponseEntity.ok(insumoService.obtenerPorId(id));
    }

    @Operation(summary = "Crear insumo", description = "Crea un nuevo insumo (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Insumo creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - solo ADMIN")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InsumoResponse> crear(@Valid @RequestBody InsumoRequest request) {
        InsumoResponse response = insumoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar insumo", description = "Actualiza un insumo existente (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Insumo actualizado"),
        @ApiResponse(responseCode = "404", description = "Insumo no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InsumoResponse> actualizar(
            @Parameter(description = "ID del insumo") @PathVariable Long id,
            @Valid @RequestBody InsumoRequest request) {
        return ResponseEntity.ok(insumoService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar insumo", description = "Elimina un insumo (soft delete, solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Insumo eliminado"),
        @ApiResponse(responseCode = "404", description = "Insumo no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del insumo") @PathVariable Long id) {
        insumoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
