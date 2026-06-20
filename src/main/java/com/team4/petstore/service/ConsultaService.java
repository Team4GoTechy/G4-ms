package com.team4.petstore.service;

import com.team4.petstore.dto.request.ConsultaRequest;
import com.team4.petstore.dto.response.ConsultaResponse;
import com.team4.petstore.entity.Cita;
import com.team4.petstore.entity.Consulta;
import com.team4.petstore.entity.Mascota;
import com.team4.petstore.entity.Veterinario;
import com.team4.petstore.entity.enums.EstadoCita;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.CitaRepository;
import com.team4.petstore.repository.ConsultaRepository;
import com.team4.petstore.repository.MascotaRepository;
import com.team4.petstore.repository.VeterinarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;

    public ConsultaService(ConsultaRepository consultaRepository,
                           CitaRepository citaRepository,
                           MascotaRepository mascotaRepository,
                           VeterinarioRepository veterinarioRepository) {
        this.consultaRepository = consultaRepository;
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Transactional
    public ConsultaResponse registrar(ConsultaRequest dto) {
        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Mascota no encontrada con id: " + dto.getMascotaId()));

        Veterinario veterinario = veterinarioRepository.findByUsuarioId(dto.getVeterinarioId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Veterinario no encontrado para usuario id: " + dto.getVeterinarioId()));

        Consulta consulta = new Consulta();
        consulta.setMascota(mascota);
        consulta.setVeterinario(veterinario);
        consulta.setMotivo(dto.getMotivo());
        consulta.setAnamnesis(dto.getAnamnesis());
        consulta.setExamenFisico(dto.getExamenFisico());
        consulta.setDiagnostico(dto.getDiagnostico());
        consulta.setTratamiento(dto.getTratamiento());
        consulta.setPeso(dto.getPeso());
        consulta.setTemperatura(dto.getTemperatura());
        consulta.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        consulta.setFrecuenciaRespiratoria(dto.getFrecuenciaRespiratoria());
        consulta.setTrc(dto.getTrc());
        consulta.setNotas(dto.getNotas());

        if (dto.getCitaId() != null) {
            Cita cita = citaRepository.findById(dto.getCitaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Cita no encontrada con id: " + dto.getCitaId()));
            consulta.setCita(cita);
            cita.setEstado(EstadoCita.COMPLETADA);
            citaRepository.save(cita);
        }

        return mapToResponse(consultaRepository.save(consulta));
    }

    @Transactional(readOnly = true)
    public ConsultaResponse buscarPorId(Long id) {
        Consulta consulta = consultaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Consulta no encontrada con id: " + id));
        return mapToResponse(consulta);
    }

    @Transactional(readOnly = true)
    public Page<ConsultaResponse> historialPorMascota(Long mascotaId, Pageable pageable) {
        mascotaRepository.findById(mascotaId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Mascota no encontrada con id: " + mascotaId));
        return consultaRepository
            .findHistorialByMascotaId(mascotaId, pageable)
            .map(this::mapToResponse);
    }

    private ConsultaResponse mapToResponse(Consulta c) {
        ConsultaResponse dto = new ConsultaResponse();
        dto.setId(c.getId());
        dto.setCitaId(c.getCita() != null ? c.getCita().getId() : null);
        dto.setMascotaId(c.getMascota().getId());
        dto.setMascotaNombre(c.getMascota().getNombre());
        dto.setVeterinarioId(c.getVeterinario().getId());
        dto.setVeterinarioNombre(c.getVeterinario().getUsuario().getNombre());
        dto.setMotivo(c.getMotivo());
        dto.setAnamnesis(c.getAnamnesis());
        dto.setExamenFisico(c.getExamenFisico());
        dto.setDiagnostico(c.getDiagnostico());
        dto.setTratamiento(c.getTratamiento());
        dto.setPeso(c.getPeso());
        dto.setTemperatura(c.getTemperatura());
        dto.setFrecuenciaCardiaca(c.getFrecuenciaCardiaca());
        dto.setFrecuenciaRespiratoria(c.getFrecuenciaRespiratoria());
        dto.setTrc(c.getTrc());
        dto.setNotas(c.getNotas());
        dto.setFechaCreacion(c.getFechaCreacion());
        return dto;
    }
}