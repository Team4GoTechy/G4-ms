package com.team4.petstore.controller;

import com.team4.petstore.dto.request.CompraRequest;
import com.team4.petstore.dto.response.CompraResponse;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.service.CompraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;
    private final UsuarioRepository usuarioRepository;

    public CompraController(CompraService compraService, UsuarioRepository usuarioRepository) {
        this.compraService = compraService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<CompraResponse> crearCompra(
            @Valid @RequestBody CompraRequest request,
            Authentication authentication) {
        Long usuarioId = getUsuarioId(authentication);
        CompraResponse response = compraService.crearCompra(usuarioId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CompraResponse>> obtenerHistorial(Authentication authentication) {
        Long usuarioId = getUsuarioId(authentication);
        return ResponseEntity.ok(compraService.obtenerHistorial(usuarioId));
    }

    private Long getUsuarioId(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getId();
    }
}
