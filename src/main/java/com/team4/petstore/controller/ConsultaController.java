package com.team4.petstore.controller;

import com.team4.petstore.dto.request.ConsultaRequest;
import com.team4.petstore.dto.request.PrescripcionRequest;
import com.team4.petstore.dto.response.ConsultaResponse;
import com.team4.petstore.dto.response.PrescripcionResponse;
import com.team4.petstore.service.ConsultaService;
import com.team4.petstore.service.PrescripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/consultas")
@Tag(name = "Consultas", description = "Registro de consultas clínicas y gestión de prescripciones asociadas")
public class ConsultaController {

    private final ConsultaService consultaService;
    private final PrescripcionService prescripcionService;

    public ConsultaController(ConsultaService consultaService,
                              PrescripcionService prescripcionService) {
        this.consultaService = consultaService;
        this.prescripcionService = prescripcionService;
    }

    @Operation(summary = "Registrar consulta", description = "Registra una nueva consulta clínica con diagnóstico, tratamiento y signos vitales. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Consulta registrada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Mascota, veterinario o cita no encontrada")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<ConsultaResponse> registrar(
            @Valid @RequestBody ConsultaRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.registrar(dto));
    }

    @Operation(summary = "Buscar consulta por ID", description = "Obtiene una consulta específica con todos sus datos. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta encontrada"),
        @ApiResponse(responseCode = "404", description = "Consulta no encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<ConsultaResponse> buscarPorId(@Parameter(description = "ID de la consulta") @PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @Operation(summary = "Agregar prescripción a la consulta", description = "Crea una prescripción asociada a la consulta. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Prescripción creada"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o la consulta ya tiene prescripción"),
        @ApiResponse(responseCode = "404", description = "Consulta o insumo no encontrado")
    })
    @PostMapping("/{id}/prescripcion")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<PrescripcionResponse> agregarPrescripcion(
            @Parameter(description = "ID de la consulta") @PathVariable Long id,
            @Valid @RequestBody PrescripcionRequest dto,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        dto.setConsultaId(id);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(prescripcionService.crear(dto, userDetails.getUsername()));
    }

    @Operation(summary = "Ver prescripción de la consulta", description = "Obtiene la prescripción asociada a una consulta. Roles: ADMIN, VETERINARIO.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prescripción encontrada"),
        @ApiResponse(responseCode = "404", description = "La consulta no tiene prescripción")
    })
    @GetMapping("/{id}/prescripcion")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<PrescripcionResponse> verPrescripcion(@Parameter(description = "ID de la consulta") @PathVariable Long id) {
        return ResponseEntity.ok(prescripcionService.buscarPorConsulta(id));
    }
}
