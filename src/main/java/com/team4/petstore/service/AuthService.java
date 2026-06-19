package com.team4.petstore.service;

import com.team4.petstore.dto.request.LoginRequest;
import com.team4.petstore.dto.request.MascotaRequest;
import com.team4.petstore.dto.request.RegisterRequest;
import com.team4.petstore.dto.response.AuthResponse;
import com.team4.petstore.entity.Mascota;
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

        AuthResponse response = new AuthResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getDireccion(),
            usuario.getCelular(),
            usuario.getEmail(),
            token
        );

        // Extraer avatar del usuario
        response.setAvatar(usuario.getAvatar());

        // Extraer informacion de la primera mascota si existe
        if (usuario.getMascotas() != null && !usuario.getMascotas().isEmpty()) {
            Mascota primeraMascota = usuario.getMascotas().get(0);
            response.setNombreMascota(primeraMascota.getNombre());
            response.setTipoMascota(primeraMascota.getTipo());
            response.setCantidadMascotas(usuario.getMascotas().size());
        } else {
            response.setCantidadMascotas(0);
        }

        return response;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        // Validar familyName si hay 3 o más mascotas
        if (request.getCantidadMascotas() != null && request.getCantidadMascotas() >= 3) {
            if (request.getFamilyName() == null || request.getFamilyName().isBlank()) {
                throw new BadRequestException("El nombre de familia es obligatorio cuando se registran 3 o más mascotas");
            }
        }

        // Validar que la cantidad de mascotas coincida con la lista enviada
        if (request.getMascotas() != null && request.getCantidadMascotas() != null) {
            if (request.getMascotas().size() != request.getCantidadMascotas()) {
                throw new BadRequestException("La cantidad de mascotas no coincide con la lista enviada");
            }
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellido(request.getApellido());
        usuario.setEdad(request.getEdad());
        usuario.setAvatar(request.getAvatar());
        usuario.setDireccion(request.getDireccion());
        usuario.setCelular(request.getCelular());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
            .orElseThrow(() -> new BadRequestException("Rol ROLE_CLIENTE no encontrado"));
        usuario.addRol(rolCliente);

        // Guardar usuario primero para obtener el ID
        usuario = usuarioRepository.save(usuario);

        // Registrar mascotas si existen
        if (request.getMascotas() != null && !request.getMascotas().isEmpty()) {
            for (MascotaRequest mascotaRequest : request.getMascotas()) {
                Mascota mascota = new Mascota(
                    mascotaRequest.getNombre(),
                    mascotaRequest.getSexo(),
                    mascotaRequest.getTipo(),
                    usuario
                );
                usuario.addMascota(mascota);
            }
            usuario = usuarioRepository.save(usuario);
        }

        String token = jwtTokenProvider.generateTokenFromEmail(usuario.getEmail());

        AuthResponse response = new AuthResponse(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getDireccion(),
            usuario.getCelular(),
            usuario.getEmail(),
            token
        );

        // Extraer avatar del usuario
        response.setAvatar(usuario.getAvatar());

        // Extraer informacion de la primera mascota si existe
        if (usuario.getMascotas() != null && !usuario.getMascotas().isEmpty()) {
            Mascota primeraMascota = usuario.getMascotas().get(0);
            response.setNombreMascota(primeraMascota.getNombre());
            response.setTipoMascota(primeraMascota.getTipo());
            response.setCantidadMascotas(usuario.getMascotas().size());
        } else {
            response.setCantidadMascotas(0);
        }

        return response;
    }
}
