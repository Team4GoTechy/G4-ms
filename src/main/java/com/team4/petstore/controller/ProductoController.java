package com.team4.petstore.controller;

import com.team4.petstore.dto.request.ProductoRequest;
import com.team4.petstore.dto.response.ProductoResponse;
import com.team4.petstore.service.ProductoService;
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
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Gestión de productos del PetStore")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Listar productos", description = "Obtiene todos los productos o filtra por categoría")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<ProductoResponse>> listar(
            @Parameter(description = "ID de categoría para filtrar") @RequestParam(required = false) Long categoriaId) {
        List<ProductoResponse> productos;
        if (categoriaId != null) {
            productos = productoService.listarPorCategoria(categoriaId);
        } else {
            productos = productoService.listar();
        }
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener producto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> obtenerPorId(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    @Operation(summary = "Crear producto", description = "Crea un nuevo producto (solo ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponse> guardar(@Valid @RequestBody ProductoRequest request) {
        ProductoResponse response = productoService.guardar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza un producto existente (solo ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoResponse> actualizar(
            @Parameter(description = "ID del producto") @PathVariable Long id,
            @Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto (solo ADMIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del producto") @PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
