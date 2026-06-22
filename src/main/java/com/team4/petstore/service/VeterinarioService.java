package com.team4.petstore.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.team4.petstore.dto.request.VeterinarioRequest;
import com.team4.petstore.dto.response.VeterinarioResponse;
import com.team4.petstore.entity.Rol;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.entity.Veterinario;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.RolRepository;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.repository.VeterinarioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public VeterinarioService(VeterinarioRepository veterinarioRepository, UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.veterinarioRepository = veterinarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Transactional
    public VeterinarioResponse crear(VeterinarioRequest dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Rol rolVet = rolRepository.findByNombre("ROLE_VETERINARIO")
            .orElseThrow(() -> new ResourceNotFoundException("Rol ROLE_VETERINARIO no encontrado"));
        usuario.addRol(rolVet);
        usuarioRepository.save(usuario);

        Veterinario v = new Veterinario();
        v.setUsuario(usuario);
        v.setMatricula(dto.getMatricula());
        v.setEspecialidad(dto.getEspecialidad());
        v.setBio(dto.getBio());
        v.setActivo(true);

        return mapToResponse(veterinarioRepository.save(v));
    }

    @Transactional(readOnly = true)
    public List<VeterinarioResponse> listarTodos() {
        return veterinarioRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public VeterinarioResponse actualizar(Long id, VeterinarioRequest dto) {
        Veterinario v = veterinarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
        v.setMatricula(dto.getMatricula());
        v.setEspecialidad(dto.getEspecialidad());
        v.setBio(dto.getBio());
        return mapToResponse(veterinarioRepository.save(v));
    }

    @Transactional
    public void eliminar(Long id) {
        Veterinario v = veterinarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
        Usuario usuario = v.getUsuario();
        if (usuario != null) {
            Rol rolVet = rolRepository.findByNombre("ROLE_VETERINARIO").orElse(null);
            if (rolVet != null) {
                usuario.getRoles().remove(rolVet);
                usuarioRepository.save(usuario);
            }
        }
        veterinarioRepository.delete(v);
    }

    private VeterinarioResponse mapToResponse(Veterinario v) {
        return new VeterinarioResponse(
            v.getId(),
            v.getUsuario().getId(),
            v.getUsuario().getNombre(),
            v.getMatricula(),
            v.getEspecialidad(),
            v.getBio(),
            v.getActivo(),
            v.getUsuario().getAvatar()
        );
    }
}
