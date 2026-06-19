package com.team4.petstore.service;

import com.team4.petstore.dto.request.EvolucionRequest;
import com.team4.petstore.dto.request.InternacionRequest;
import com.team4.petstore.dto.response.EvolucionResponse;
import com.team4.petstore.dto.response.InternacionResponse;
import com.team4.petstore.entity.*;
import com.team4.petstore.entity.enums.EstadoInternacion;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class InternacionService {

    private final InternacionRepository internacionRepository;
    private final EvolucionInternacionRepository evolucionRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final UsuarioRepository usuarioRepository;

    public InternacionService(InternacionRepository internacionRepository,
                              EvolucionInternacionRepository evolucionRepository,
                              MascotaRepository mascotaRepository,
                              VeterinarioRepository veterinarioRepository,
                              UsuarioRepository usuarioRepository) {
        this.internacionRepository = internacionRepository;
        this.evolucionRepository = evolucionRepository;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public InternacionResponse ingresar(InternacionRequest dto) {
        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Mascota no encontrada con id: " + dto.getMascotaId()));

        Veterinario veterinario = veterinarioRepository.findById(dto.getVeterinarioId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Veterinario no encontrado con id: " + dto.getVeterinarioId()));

        Internacion internacion = new Internacion();
        internacion.setMascota(mascota);
        internacion.setVeterinario(veterinario);
        internacion.setMotivo(dto.getMotivo());
        internacion.setJaulaId(dto.getJaulaId());
        internacion.setNotas(dto.getNotas());
        internacion.setEstado(EstadoInternacion.ACTIVA);

        return mapToResponse(internacionRepository.save(internacion));
    }

    @Transactional
    public EvolucionResponse registrarEvolucion(Long internacionId,
                                                    EvolucionRequest dto,
                                                    String emailUsuario) {
        Internacion internacion = internacionRepository.findById(internacionId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Internación no encontrada con id: " + internacionId));

        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Usuario no encontrado: " + emailUsuario));

        EvolucionInternacion evolucion = new EvolucionInternacion();
        evolucion.setInternacion(internacion);
        evolucion.setUsuario(usuario);
        evolucion.setFechaHora(LocalDateTime.now());
        evolucion.setObservacion(dto.getObservacion());
        evolucion.setPeso(dto.getPeso());
        evolucion.setTemperatura(dto.getTemperatura());

        return mapEvolucionToResponse(evolucionRepository.save(evolucion));
    }

    @Transactional
    public InternacionResponse darDeAlta(Long internacionId) {
        Internacion internacion = internacionRepository.findById(internacionId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Internación no encontrada con id: " + internacionId));

        internacion.setEstado(EstadoInternacion.ALTA);
        internacion.setFechaAlta(LocalDateTime.now());
        return mapToResponse(internacionRepository.save(internacion));
    }

    @Transactional(readOnly = true)
    public List<InternacionResponse> listarActivas() {
        List<InternacionResponse> resultado = new ArrayList<>();
        for (Internacion i : internacionRepository.findByEstado(EstadoInternacion.ACTIVA)) {
            resultado.add(mapToResponse(i));
        }
        return resultado;
    }

    private InternacionResponse mapToResponse(Internacion i) {
        InternacionResponse dto = new InternacionResponse();
        dto.setId(i.getId());
        dto.setMascotaId(i.getMascota().getId());
        dto.setMascotaNombre(i.getMascota().getNombre());
        dto.setVeterinarioId(i.getVeterinario().getId());
        dto.setVeterinarioNombre(i.getVeterinario().getUsuario().getNombre());
        dto.setMotivo(i.getMotivo());
        dto.setFechaIngreso(i.getFechaIngreso());
        dto.setFechaAlta(i.getFechaAlta());
        dto.setJaulaId(i.getJaulaId());
        dto.setEstado(i.getEstado());
        dto.setNotas(i.getNotas());

        List<EvolucionResponse> evolucionesDto = new ArrayList<>();
        for (EvolucionInternacion e : i.getEvoluciones()) {
            evolucionesDto.add(mapEvolucionToResponse(e));
        }
        dto.setEvoluciones(evolucionesDto);
        return dto;
    }

    private EvolucionResponse mapEvolucionToResponse(EvolucionInternacion e) {
        EvolucionResponse dto = new EvolucionResponse();
        dto.setId(e.getId());
        dto.setInternacionId(e.getInternacion().getId());
        dto.setUsuarioNombre(e.getUsuario() != null ? e.getUsuario().getNombre() : null);
        dto.setFechaHora(e.getFechaHora());
        dto.setObservacion(e.getObservacion());
        dto.setPeso(e.getPeso());
        dto.setTemperatura(e.getTemperatura());
        return dto;
    }
}