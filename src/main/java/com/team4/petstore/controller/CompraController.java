package com.team4.petstore.controller;

import com.team4.petstore.dto.request.CompraRequest;
import com.team4.petstore.dto.response.CompraResponse;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.service.CompraService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compras")
@Tag(name = "Compras", description = "Gestión de compras. Usuarios: historial y creación. ADMIN: cambio de estado")
public class CompraController {

    private final CompraService compraService;
    private final UsuarioRepository usuarioRepository;

    public CompraController(CompraService compraService, UsuarioRepository usuarioRepository) {
        this.compraService = compraService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Crear compra", description = "Crea una nueva compra para el usuario autenticado. Descuenta stock automáticamente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Compra creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o stock insuficiente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping
    public ResponseEntity<CompraResponse> crearCompra(
            @Valid @RequestBody CompraRequest request,
            Authentication authentication) {
        Long usuarioId = getUsuarioId(authentication);
        CompraResponse response = compraService.crearCompra(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener historial de compras", description = "Devuelve el historial de compras del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Historial obtenido correctamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @GetMapping
    public ResponseEntity<List<CompraResponse>> obtenerHistorial(Authentication authentication) {
        Long usuarioId = getUsuarioId(authentication);
        return ResponseEntity.ok(compraService.obtenerHistorial(usuarioId));
    }

    @Operation(summary = "Cambiar estado de compra", description = "Permite al ADMIN cambiar el estado de una compra. Cuando se cancela, el stock se restaura automáticamente. Estados válidos: PENDIENTE, CONFIRMADO, ENTREGADO, CANCELADO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido o stock insuficiente al intentar confirmar"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - solo ADMIN"),
        @ApiResponse(responseCode = "404", description = "Compra no encontrada")
    })
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompraResponse> updateEstado(
            @Parameter(description = "ID de la compra", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado: PENDIENTE, CONFIRMADO, ENTREGADO o CANCELADO", required = true, example = "CONFIRMADO")
            @RequestParam String estado) {
        CompraResponse response = compraService.updateEstado(id, estado);
        return ResponseEntity.ok(response);
    }

    private Long getUsuarioId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getId();
    }
}
