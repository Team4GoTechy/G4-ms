package com.team4.petstore.controller;

import com.team4.petstore.dto.request.CitaRequest;
import com.team4.petstore.dto.request.EstadoCitaRequest;
import com.team4.petstore.dto.response.CitaResponse;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")
@Tag(name = "Citas", description = "Gestión de citas veterinarias (creación, agenda, estado y pago)")
public class CitaController {

    private final CitaService citaService;
    private final UsuarioRepository usuarioRepository;

    public CitaController(CitaService citaService, UsuarioRepository usuarioRepository) {
        this.citaService = citaService;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(summary = "Crear cita", description = "Crea una nueva cita. Valida que el veterinario ofrezca el servicio solicitado. Roles: ADMIN, VETERINARIO, CLIENTE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cita creada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o el veterinario no ofrece el servicio"),
        @ApiResponse(responseCode = "404", description = "Mascota, veterinario o servicio no encontrado")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ROLE_CLIENTE', 'ADMIN', 'VETERINARIO', 'DOCTOR', 'CLIENTE')")
    public ResponseEntity<CitaResponse> crear(@Valid @RequestBody CitaRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(citaService.crear(dto));
    }

    @Operation(summary = "Buscar cita por ID", description = "Obtiene una cita específica. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita encontrada"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<CitaResponse> buscarPorId(@Parameter(description = "ID de la cita") @PathVariable Long id) {
        return ResponseEntity.ok(citaService.buscarPorId(id));
    }

    @Operation(summary = "Agenda diaria del veterinario", description = "Devuelve las citas de un veterinario en un día específico. Roles: ADMIN, VETERINARIO, CLIENTE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agenda del día")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ROLE_CLIENTE', 'ADMIN', 'VETERINARIO', 'DOCTOR', 'CLIENTE')")
    public ResponseEntity<List<CitaResponse>> obtenerAgenda(
            @Parameter(description = "ID del veterinario") @RequestParam Long veterinarioId,
            @Parameter(description = "Fecha en formato YYYY-MM-DD") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(citaService.obtenerAgendaDelDia(veterinarioId, fecha));
    }

    @Operation(summary = "Agenda mensual del veterinario", description = "Devuelve las citas de un veterinario en un mes completo. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agenda del mes")
    })
    @GetMapping("/agenda/mes")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<List<CitaResponse>> obtenerAgendaDelMes(
            @Parameter(description = "ID del veterinario") @RequestParam Long veterinarioId,
            @Parameter(description = "Mes en formato YYYY-MM") @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth mes) {
        return ResponseEntity.ok(citaService.obtenerAgendaDelMes(veterinarioId, mes));
    }

    @Operation(summary = "Todas las citas del veterinario", description = "Lista completa de citas de un veterinario sin filtro de fecha. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado completo")
    })
    @GetMapping("/todas")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<List<CitaResponse>> obtenerTodasLasCitas(
            @Parameter(description = "ID del veterinario") @RequestParam Long veterinarioId) {
        return ResponseEntity.ok(citaService.obtenerTodasLasCitas(veterinarioId));
    }

    @Operation(summary = "Cambiar estado de la cita", description = "Actualiza el estado (PENDIENTE, CONFIRMADA, EN_PROGRESO, COMPLETADA, CANCELADA, NO_ASISTIO). El campo 'motivo' se persiste en motivoCancelacion. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<CitaResponse> actualizarEstado(
            @Parameter(description = "ID de la cita") @PathVariable Long id,
            @Valid @RequestBody EstadoCitaRequest dto) {
        return ResponseEntity.ok(citaService.actualizarEstado(id, dto));
    }

    @Operation(summary = "Mis citas (cliente)", description = "Lista las citas del cliente autenticado. Roles: CLIENTE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Citas del cliente"),
        @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    @GetMapping("/mis-citas")
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'CLIENTE')")
    public ResponseEntity<List<CitaResponse>> obtenerMisCitas(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(citaService.obtenerCitasCliente(usuario.getId()));
    }

    @Operation(summary = "Pagar cita", description = "Marca la cita como pagada. Roles: CLIENTE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cita pagada"),
        @ApiResponse(responseCode = "404", description = "Cita no encontrada")
    })
    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'CLIENTE')")
    public ResponseEntity<CitaResponse> pagarCita(@Parameter(description = "ID de la cita") @PathVariable Long id) {
        return ResponseEntity.ok(citaService.pagarCita(id));
    }
}
