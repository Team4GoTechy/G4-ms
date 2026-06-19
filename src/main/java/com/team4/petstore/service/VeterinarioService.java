package com.team4.petstore.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.team4.petstore.dto.request.VeterinarioRequest;
import com.team4.petstore.dto.response.VeterinarioResponse;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.entity.Veterinario;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.repository.VeterinarioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final UsuarioRepository usuarioRepository;

    public VeterinarioService(VeterinarioRepository veterinarioRepository, UsuarioRepository usuarioRepository) {
        this.veterinarioRepository = veterinarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public VeterinarioResponse crear(VeterinarioRequest dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

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
        veterinarioRepository.deleteById(id);
    }

    private VeterinarioResponse mapToResponse(Veterinario v) {
        return new VeterinarioResponse(
            v.getId(),
            v.getUsuario().getNombre(),
            v.getMatricula(),
            v.getEspecialidad(),
            v.getBio(),
            v.getActivo()
        );
    }
}
