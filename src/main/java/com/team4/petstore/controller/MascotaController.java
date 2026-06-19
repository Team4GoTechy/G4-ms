package com.team4.petstore.controller;

import com.team4.petstore.dto.response.MascotaResponse;
import com.team4.petstore.service.CitaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mascotas")
public class MascotaController {

    private final CitaService citaService;

    public MascotaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @GetMapping
    public ResponseEntity<List<MascotaResponse>> listarTodas() {
        return ResponseEntity.ok(citaService.obtenerTodasLasMascotas());
    }
}
