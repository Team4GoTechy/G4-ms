package com.team4.petstore.controller;

import com.team4.petstore.dto.request.ServicioRequest;
import com.team4.petstore.dto.response.ServicioResponse;
import com.team4.petstore.dto.response.VeterinarioResponse;
import com.team4.petstore.service.ServicioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/servicios")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicioResponse> crear(@Valid @RequestBody ServicioRequest dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioService.crear(dto));
    }
    
    @GetMapping("/veterinarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VeterinarioResponse>> obtenerTodosLosVeterinarios() {
        return ResponseEntity.ok(servicioService.obtenerTodosLosVeterinarios());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ROLE_CLIENTE', 'ADMIN', 'VETERINARIO', 'DOCTOR', 'CLIENTE')")
    public ResponseEntity<List<ServicioResponse>> listarTodos() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ServicioResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ServicioRequest dto) {
        return ResponseEntity.ok(servicioService.actualizar(id, dto));
    }

    @GetMapping("/veterinario/{veterinarioId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_VETERINARIO', 'ROLE_DOCTOR', 'ROLE_CLIENTE', 'ADMIN', 'VETERINARIO', 'DOCTOR', 'CLIENTE')")
    public ResponseEntity<List<ServicioResponse>> listarPorVeterinario(@PathVariable Long veterinarioId) {
        return ResponseEntity.ok(servicioService.listarPorVeterinario(veterinarioId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        servicioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
