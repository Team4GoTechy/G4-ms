package com.team4.petstore.controller;

import com.team4.petstore.dto.request.SolicitudReposicionRequest;
import com.team4.petstore.dto.response.SolicitudReposicionResponse;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.service.SolicitudReposicionService;
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
@RequestMapping("/solicitudes")
@Tag(name = "Solicitudes de Reposicion", description = "Gestion de solicitudes de reposicion de insumos")
public class SolicitudReposicionController {

    private final SolicitudReposicionService solicitudService;
    private final UsuarioRepository usuarioRepository;

    public SolicitudReposicionController(SolicitudReposicionService solicitudService,
                                       UsuarioRepository usuarioRepository) {
        this.solicitudService = solicitudService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Listar solicitudes propias", description = "Obtiene las solicitudes del veterinario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida correctamente")
    })
    @GetMapping
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<List<SolicitudReposicionResponse>> listarMisSolicitudes(Authentication authentication) {
        Long veterinarioId = getUsuarioId(authentication);
        return ResponseEntity.ok(solicitudService.listarPorVeterinario(veterinarioId));
    }

    @Operation(summary = "Listar todas las solicitudes", description = "Obtiene todas las solicitudes (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - solo ADMIN")
    })
    @GetMapping("/todas")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SolicitudReposicionResponse>> listarTodas() {
        return ResponseEntity.ok(solicitudService.listarTodas());
    }

    @Operation(summary = "Obtener solicitudes de un veterinario", description = "Obtiene las solicitudes de un veterinario especifico (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de solicitudes obtenida correctamente"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - solo ADMIN")
    })
    @GetMapping("/veterinario/{veterinarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SolicitudReposicionResponse>> listarPorVeterinario(
            @Parameter(description = "ID del veterinario") @PathVariable Long veterinarioId) {
        return ResponseEntity.ok(solicitudService.listarPorVeterinarioId(veterinarioId));
    }

    @Operation(summary = "Obtener solicitud por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud encontrada"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VETERINARIO')")
    public ResponseEntity<SolicitudReposicionResponse> obtenerPorId(
            @Parameter(description = "ID de la solicitud") @PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.obtenerPorId(id));
    }

    @Operation(summary = "Crear solicitud", description = "Crea una nueva solicitud de reposicion (solo VET)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Solicitud creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos invalidos o insumos no encontrados"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado - solo VETERINARIO")
    })
    @PostMapping
    @PreAuthorize("hasRole('VETERINARIO')")
    public ResponseEntity<SolicitudReposicionResponse> crear(
            @Valid @RequestBody SolicitudReposicionRequest request,
            Authentication authentication) {
        Long veterinarioId = getUsuarioId(authentication);
        SolicitudReposicionResponse response = solicitudService.crear(veterinarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Aprobar solicitud", description = "Aprueba una solicitud y genera una orden de compra (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud aprobada correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud no esta en estado PENDIENTE"),
        @ApiResponse(responseCode = "404", description = "Solicitud o proveedor no encontrado")
    })
    @PutMapping("/{id}/aprobar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SolicitudReposicionResponse> aprobar(
            @Parameter(description = "ID de la solicitud") @PathVariable Long id,
            @Parameter(description = "ID del proveedor", required = true) @RequestParam Long proveedorId) {
        return ResponseEntity.ok(solicitudService.aprobar(id, proveedorId));
    }

    @Operation(summary = "Cancelar solicitud", description = "Cancela una solicitud (solo ADMIN)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Solicitud cancelada correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud no esta en estado PENDIENTE"),
        @ApiResponse(responseCode = "404", description = "Solicitud no encontrada")
    })
    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SolicitudReposicionResponse> cancelar(
            @Parameter(description = "ID de la solicitud") @PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.cancelar(id));
    }

    private Long getUsuarioId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getId();
    }
}
