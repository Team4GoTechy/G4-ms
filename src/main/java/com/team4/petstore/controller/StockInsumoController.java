package com.team4.petstore.controller;

import com.team4.petstore.dto.request.ConsumoRequest;
import com.team4.petstore.dto.response.MovimientoInsumoResponse;
import com.team4.petstore.dto.response.StockInsumoResponse;
import com.team4.petstore.service.StockInsumoService;
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
@RequestMapping("/insumos")
@Tag(name = "Stock Insumos", description = "Gestion de stock y consumo de insumos")
public class StockInsumoController {

    private final StockInsumoService stockService;

    public StockInsumoController(StockInsumoService stockService) {
        this.stockService = stockService;
    }

    @Operation(summary = "Ver stock de todos los insumos", description = "Obtiene el stock actual de todos los insumos (VET y ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock obtenido correctamente")
    })
    @GetMapping("/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public ResponseEntity<List<StockInsumoResponse>> listarStock() {
        return ResponseEntity.ok(stockService.listarStock());
    }

    @Operation(summary = "Ver stock de un insumo", description = "Obtiene el stock actual de un insumo especifico (VET y ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Stock no encontrado")
    })
    @GetMapping("/stock/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public ResponseEntity<StockInsumoResponse> obtenerStockPorInsumo(
            @Parameter(description = "ID del insumo") @PathVariable Long id) {
        return ResponseEntity.ok(stockService.obtenerStockPorInsumoId(id));
    }

    @Operation(summary = "Ver historial de movimientos", description = "Obtiene el historial de movimientos de un insumo (VET y ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente")
    })
    @GetMapping("/stock/{id}/historial")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public ResponseEntity<List<MovimientoInsumoResponse>> obtenerHistorial(
            @Parameter(description = "ID del insumo") @PathVariable Long id) {
        return ResponseEntity.ok(stockService.obtenerHistorial(id));
    }

    @Operation(summary = "Registrar consumo", description = "Registra el consumo de insumos (solo VET)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consumo registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Stock insuficiente o datos invalidos"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - solo VETERINARIO")
    })
    @PostMapping("/consumo")
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<Void> registrarConsumo(@Valid @RequestBody ConsumoRequest request) {
        stockService.registrarConsumo(request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Stock disponible para frontend", description = "Endpoint publico para mostrar stock disponible")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock obtenido correctamente")
    })
    @GetMapping("/stock/disponibles")
    public ResponseEntity<List<StockInsumoResponse>> listarStockDisponibles() {
        return ResponseEntity.ok(stockService.listarStockDisponibles());
    }
}
