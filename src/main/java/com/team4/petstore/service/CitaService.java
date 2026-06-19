package com.team4.petstore.service;

import com.team4.petstore.dto.request.CitaRequest;
import com.team4.petstore.dto.request.EstadoCitaRequest;
import com.team4.petstore.dto.response.CitaResponse;
import com.team4.petstore.dto.response.MascotaResponse;
import com.team4.petstore.entity.Cita;
import com.team4.petstore.entity.Mascota;
import com.team4.petstore.entity.Veterinario;
import com.team4.petstore.entity.enums.EstadoCita;
import com.team4.petstore.exception.CitaSolapadaException;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.CitaRepository;
import com.team4.petstore.repository.MascotaRepository;
import com.team4.petstore.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;
    private final VeterinarioRepository veterinarioRepository;

    public CitaService(CitaRepository citaRepository,
                       MascotaRepository mascotaRepository,
                       VeterinarioRepository veterinarioRepository) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    @Transactional
    public CitaResponse crear(CitaRequest dto) {
        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Mascota no encontrada con id: " + dto.getMascotaId()));

        Veterinario veterinario = veterinarioRepository.findById(dto.getVeterinarioId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Veterinario no encontrado con id: " + dto.getVeterinarioId()));

        LocalDateTime inicio = dto.getFechaHora();
        LocalDateTime fin = inicio.plusMinutes(dto.getDuracionMinutos());

        if (citaRepository.existeSolapamiento(veterinario.getId(), inicio, fin, null)) {
            throw new CitaSolapadaException(
                "El veterinario ya tiene una cita en ese horario");
        }

        Cita cita = new Cita();
        cita.setMascota(mascota);
        cita.setVeterinario(veterinario);
        cita.setTipoCita(dto.getTipoCita());
        cita.setFechaHora(inicio);
        cita.setDuracionMinutos(dto.getDuracionMinutos());
        cita.setNotas(dto.getNotas());
        cita.setEstado(EstadoCita.PENDIENTE);

        return mapToResponse(citaRepository.save(cita));
    }
    
    @Transactional(readOnly = true)
    public List<MascotaResponse> obtenerTodasLasMascotas() {
        return mascotaRepository.findAll()
            .stream()
            .map(m -> new MascotaResponse(
                m.getId(),
                m.getNombre(),
                m.getSexo(),
                m.getTipo()
            ))
            .collect(Collectors.toList());
    }

    @Transactional
    public CitaResponse actualizarEstado(Long citaId, EstadoCitaRequest dto) {
        Cita cita = citaRepository.findById(citaId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Cita no encontrada con id: " + citaId));

        cita.setEstado(dto.getEstado());
        return mapToResponse(citaRepository.save(cita));
    }

    @Transactional(readOnly = true)
    public CitaResponse buscarPorId(Long id) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Cita no encontrada con id: " + id));
        return mapToResponse(cita);
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerAgendaDelDia(Long veterinarioId, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return citaRepository
            .findByVeterinarioIdAndFechaHoraBetween(veterinarioId, inicio, fin)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerAgendaDelMes(Long veterinarioId, YearMonth mes) {
        LocalDate primerDia = mes.atDay(1);
        LocalDate ultimoDia = mes.atEndOfMonth();

        LocalDateTime inicio = primerDia.atStartOfDay();
        LocalDateTime fin = ultimoDia.atTime(LocalTime.MAX);

        return citaRepository
            .findByVeterinarioIdAndFechaHoraBetween(veterinarioId, inicio, fin)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerTodasLasCitas(Long veterinarioId) {
        return citaRepository
            .findByVeterinarioId(veterinarioId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }



    private CitaResponse mapToResponse(Cita cita) {
        CitaResponse dto = new CitaResponse();
        dto.setId(cita.getId());
        dto.setMascotaId(cita.getMascota().getId());
        dto.setMascotaNombre(cita.getMascota().getNombre());
        dto.setVeterinarioId(cita.getVeterinario().getId());
        dto.setVeterinarioNombre(cita.getVeterinario().getUsuario().getNombre());
        dto.setTipoCita(cita.getTipoCita());
        dto.setFechaHora(cita.getFechaHora());
        dto.setDuracionMinutos(cita.getDuracionMinutos());
        dto.setEstado(cita.getEstado());
        dto.setNotas(cita.getNotas());
        dto.setFechaCreacion(cita.getFechaCreacion());
        return dto;
    }
}