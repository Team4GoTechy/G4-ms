package com.team4.petstore.controller;

import com.team4.petstore.dto.response.MascotaResponse;
import com.team4.petstore.dto.response.PrescripcionResponse;
import com.team4.petstore.entity.Mascota;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.repository.MascotaRepository;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.service.CitaService;
import com.team4.petstore.service.PrescripcionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/mascotas")
public class MascotaController {

    private final CitaService citaService;
    private final MascotaRepository mascotaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrescripcionService prescripcionService;

    public MascotaController(CitaService citaService,
                             MascotaRepository mascotaRepository,
                             UsuarioRepository usuarioRepository,
                             PrescripcionService prescripcionService) {
        this.citaService = citaService;
        this.mascotaRepository = mascotaRepository;
        this.usuarioRepository = usuarioRepository;
        this.prescripcionService = prescripcionService;
    }

    @GetMapping
    public ResponseEntity<List<MascotaResponse>> listarTodas() {
        return ResponseEntity.ok(citaService.obtenerTodasLasMascotas());
    }

    @GetMapping("/mis-mascotas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<MascotaResponse>> listarMisMascotas(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<MascotaResponse> response = mascotaRepository.findByUsuarioId(usuario.getId())
            .stream()
            .map(m -> new MascotaResponse(
                m.getId(),
                m.getNombre(),
                m.getSexo(),
                m.getTipo(),
                m.getRaza(),
                m.getFechaNacimiento(),
                m.getPeso()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MascotaResponse> actualizarMascota(
            @PathVariable Long id,
            @RequestBody MascotaResponse dto,
            Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mascota mascota = mascotaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        boolean isAdminOrVet = userDetails.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().contains("ADMIN") || a.getAuthority().contains("DOCTOR") || a.getAuthority().contains("VETERINARIO"));

        if (!mascota.getUsuario().getId().equals(usuario.getId()) && !isAdminOrVet) {
            throw new IllegalArgumentException("No tienes permisos para modificar esta mascota");
        }

        if (dto.getNombre() != null) mascota.setNombre(dto.getNombre());
        if (dto.getSexo() != null) mascota.setSexo(dto.getSexo());
        if (dto.getTipo() != null) mascota.setTipo(dto.getTipo());
        if (dto.getRaza() != null) mascota.setRaza(dto.getRaza());
        if (dto.getFechaNacimiento() != null) mascota.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getPeso() != null) mascota.setPeso(dto.getPeso());

        Mascota guardada = mascotaRepository.save(mascota);
        MascotaResponse res = new MascotaResponse(
            guardada.getId(),
            guardada.getNombre(),
            guardada.getSexo(),
            guardada.getTipo(),
            guardada.getRaza(),
            guardada.getFechaNacimiento(),
            guardada.getPeso()
        );
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{mascotaId}/prescripciones")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<PrescripcionResponse>> obtenerPrescripciones(@PathVariable Long mascotaId) {
        return ResponseEntity.ok(prescripcionService.buscarPorMascota(mascotaId));
    }
}
