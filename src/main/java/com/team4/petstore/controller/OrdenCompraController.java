package com.team4.petstore.controller;

import com.team4.petstore.dto.request.OrdenCompraCompletarRequest;
import com.team4.petstore.dto.response.OrdenCompraResponse;
import com.team4.petstore.service.OrdenCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordenes-compra")
@Tag(name = "Ordenes de Compra", description = "Gestion de ordenes de compra de insumos (ADMIN)")
public class OrdenCompraController {

    private final OrdenCompraService ordenService;

    public OrdenCompraController(OrdenCompraService ordenService) {
        this.ordenService = ordenService;
    }

    @Operation(summary = "Listar ordenes de compra", description = "Obtiene todas las ordenes de compra (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ordenes obtenida correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - solo ADMIN")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrdenCompraResponse>> listar() {
        return ResponseEntity.ok(ordenService.listar());
    }

    @Operation(summary = "Obtener orden de compra por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden encontrada"),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrdenCompraResponse> obtenerPorId(
            @Parameter(description = "ID de la orden") @PathVariable Long id) {
        return ResponseEntity.ok(ordenService.obtenerPorId(id));
    }

    @Operation(summary = "Completar orden", description = "Marca la orden como completada, actualiza precios y stock (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden completada correctamente"),
        @ApiResponse(responseCode = "400", description = "Orden no esta en estado PENDIENTE"),
        @ApiResponse(responseCode = "404", description = "Orden o insumo no encontrado")
    })
    @PutMapping("/{id}/completar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrdenCompraResponse> completar(
            @Parameter(description = "ID de la orden") @PathVariable Long id,
            @Valid @RequestBody OrdenCompraCompletarRequest request) {
        return ResponseEntity.ok(ordenService.completar(id, request));
    }

    @Operation(summary = "Cancelar orden", description = "Cancela una orden de compra (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden cancelada correctamente"),
        @ApiResponse(responseCode = "400", description = "Orden no esta en estado PENDIENTE"),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrdenCompraResponse> cancelar(
            @Parameter(description = "ID de la orden") @PathVariable Long id) {
        return ResponseEntity.ok(ordenService.cancelar(id));
    }
}
