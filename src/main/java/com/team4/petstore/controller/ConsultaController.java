package com.team4.petstore.controller;

import com.team4.petstore.dto.request.ConsultaRequest;
import com.team4.petstore.dto.request.PrescripcionRequest;
import com.team4.petstore.dto.response.ConsultaResponse;
import com.team4.petstore.dto.response.PrescripcionResponse;
import com.team4.petstore.service.ConsultaService;
import com.team4.petstore.service.PrescripcionService;
import jakarta.validation.Valid;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;
    private final PrescripcionService prescripcionService;

    public ConsultaController(ConsultaService consultaService,
                              PrescripcionService prescripcionService) {
        this.consultaService = consultaService;
        this.prescripcionService = prescripcionService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_VETERINARIO')")
    public ResponseEntity<ConsultaResponse> registrar(
            @Valid @RequestBody ConsultaRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.registrar(dto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<ConsultaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarPorId(id));
    }

    @PostMapping("/{id}/prescripcion")
    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_VETERINARIO')")
    public ResponseEntity<PrescripcionResponse> agregarPrescripcion(
            @PathVariable Long id,
            @Valid @RequestBody PrescripcionRequest dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        dto.setConsultaId(id);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(prescripcionService.crear(dto, userDetails.getUsername()));
    }

    @GetMapping("/{id}/prescripcion")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ADMIN', 'VETERINARIO', 'DOCTOR')")
    public ResponseEntity<PrescripcionResponse> verPrescripcion(@PathVariable Long id) {
        return ResponseEntity.ok(prescripcionService.buscarPorConsulta(id));
    }
}
