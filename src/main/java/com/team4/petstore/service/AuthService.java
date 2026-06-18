package com.team4.petstore.service;

import com.team4.petstore.dto.request.LoginRequest;
import com.team4.petstore.dto.request.RegisterRequest;
import com.team4.petstore.dto.response.AuthResponse;
import com.team4.petstore.entity.Rol;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.exception.BadRequestException;
import com.team4.petstore.repository.RolRepository;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthenticationManager authenticationManager,
                       UsuarioRepository usuarioRepository,
                       RolRepository rolRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication);
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        return new AuthResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getEmail(),
            token
        );
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setDireccion(request.getDireccion());
        usuario.setCelular(request.getCelular());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
            .orElseThrow(() -> new BadRequestException("Rol ROLE_CLIENTE no encontrado"));
        usuario.addRol(rolCliente);

        usuario = usuarioRepository.save(usuario);

        String token = jwtTokenProvider.generateTokenFromEmail(usuario.getEmail());

        return new AuthResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getEmail(),
            token
        );
    }
}
