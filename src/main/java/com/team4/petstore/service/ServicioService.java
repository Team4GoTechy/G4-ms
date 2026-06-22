package com.team4.petstore.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.team4.petstore.dto.request.ServicioRequest;
import com.team4.petstore.dto.response.ServicioResponse;
import com.team4.petstore.dto.response.VeterinarioResponse;
import com.team4.petstore.entity.Veterinario;
import com.team4.petstore.entity.Servicio;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.ServicioRepository;
import com.team4.petstore.repository.VeterinarioRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final VeterinarioRepository veterinarioRepository;

    public ServicioService(ServicioRepository servicioRepository, VeterinarioRepository veterinarioRepository) {
        this.servicioRepository = servicioRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Transactional
    public ServicioResponse crear(ServicioRequest dto) {
        List<Veterinario> veterinarios = veterinarioRepository.findAllById(dto.getVeterinarioIds());

        if (veterinarios.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron veterinarios para los IDs proporcionados");
        }

        Servicio servicio = new Servicio();
        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecio(dto.getPrecio());
        servicio.setVeterinarios(veterinarios);

        return mapToResponse(servicioRepository.save(servicio));
    }
    
    @Transactional(readOnly = true)
    public List<VeterinarioResponse> obtenerTodosLosVeterinarios() {
        return veterinarioRepository.findAll()
            .stream()
            .map(v -> new VeterinarioResponse(
                v.getId(),
                v.getUsuario().getId(),
                v.getUsuario().getNombre(),
                v.getMatricula(),
                v.getEspecialidad(),
                v.getBio(),
                v.getActivo(),
                v.getUsuario().getAvatar()
            ))
            .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<ServicioResponse> listarPorVeterinario(Long veterinarioId) {
        return servicioRepository.findByVeterinariosId(veterinarioId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ServicioResponse> listarTodos() {
        return servicioRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public ServicioResponse actualizar(Long id, ServicioRequest dto) {
        Servicio servicio = servicioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Servicio no encontrado con ID: " + id));

        List<Veterinario> veterinarios = veterinarioRepository.findAllById(dto.getVeterinarioIds());
        if (veterinarios.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron veterinarios para los IDs proporcionados");
        }

        servicio.setNombre(dto.getNombre());
        servicio.setDescripcion(dto.getDescripcion());
        servicio.setPrecio(dto.getPrecio());
        servicio.setVeterinarios(veterinarios);

        return mapToResponse(servicioRepository.save(servicio));
    }

    @Transactional
    public void eliminar(Long id) {
        servicioRepository.deleteById(id);
    }

    private ServicioResponse mapToResponse(Servicio servicio) {
        List<VeterinarioResponse> veterinarios = servicio.getVeterinarios()
            .stream()
            .map(v -> new VeterinarioResponse(
                v.getId(),
                v.getUsuario().getId(),
                v.getUsuario().getNombre(),
                v.getMatricula(),
                v.getEspecialidad(),
                v.getBio(),
                v.getActivo(),
                v.getUsuario().getAvatar()
            ))
            .collect(Collectors.toList());

        return new ServicioResponse(
            servicio.getId(),
            servicio.getNombre(),
            servicio.getDescripcion(),
            servicio.getPrecio(),
            veterinarios
        );
    }

}
