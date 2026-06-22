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
import com.team4.petstore.repository.UsuarioRepository;
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
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    public CitaService(CitaRepository citaRepository,
                        MascotaRepository mascotaRepository,
                        VeterinarioRepository veterinarioRepository,
                        UsuarioRepository usuarioRepository,
                        NotificacionService notificacionService) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public CitaResponse crear(CitaRequest dto) {
        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Mascota no encontrada con id: " + dto.getMascotaId()));

        Veterinario veterinario = veterinarioRepository.findByUsuarioId(dto.getVeterinarioId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Veterinario no encontrado para el usuario id: " + dto.getVeterinarioId()));

        LocalDateTime inicio = dto.getFechaHora();
        LocalDateTime fin = inicio.plusMinutes(dto.getDuracionMinutos());

        if (inicio.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La cita no puede ser programada en el pasado");
        }

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

        Cita guardada = citaRepository.save(cita);

        try {
            notificacionService.crearNotificacion(
                mascota.getUsuario(),
                "Nuevo Turno Solicitado",
                "Se ha solicitado un turno de tipo " + dto.getTipoCita() + " para tu mascota " + mascota.getNombre() + " con el doctor " + veterinario.getUsuario().getNombre() + " para el " + inicio.getDayOfMonth() + "/" + inicio.getMonthValue() + " a las " + inicio.toLocalTime() + " hs.",
                "sistema"
            );
        } catch (Exception e) {
            System.err.println("Error al notificar nueva cita: " + e.getMessage());
        }

        return mapToResponse(guardada);
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

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerCitasCliente(Long clienteUsuarioId) {
        return citaRepository.findByMascotaUsuarioIdOrderByFechaHoraDesc(clienteUsuarioId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public CitaResponse actualizarEstado(Long citaId, EstadoCitaRequest dto) {
        Cita cita = citaRepository.findById(citaId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Cita no encontrada con id: " + citaId));

        EstadoCita anterior = cita.getEstado();
        cita.setEstado(dto.getEstado());
        Cita guardada = citaRepository.save(cita);

        if (anterior != dto.getEstado()) {
            try {
                String descEstado = dto.getEstado().toString();
                String titulo = "Estado de Turno Actualizado";
                if (dto.getEstado() == EstadoCita.CONFIRMADA) {
                    titulo = "Turno Confirmado";
                    descEstado = "ha sido confirmado por el doctor";
                } else if (dto.getEstado() == EstadoCita.CANCELADA) {
                    titulo = "Turno Cancelado";
                    descEstado = "ha sido cancelado";
                } else {
                    descEstado = "ha cambiado al estado: " + descEstado;
                }

                notificacionService.crearNotificacion(
                    guardada.getMascota().getUsuario(),
                    titulo,
                    "Tu turno para " + guardada.getMascota().getNombre() + " programado para el " + guardada.getFechaHora().getDayOfMonth() + "/" + guardada.getFechaHora().getMonthValue() + " " + descEstado + ".",
                    "sistema"
                );
            } catch (Exception e) {
                System.err.println("Error al notificar actualización de cita: " + e.getMessage());
            }
        }

        return mapToResponse(guardada);
    }

    @Transactional(readOnly = true)
    public CitaResponse buscarPorId(Long id) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Cita no encontrada con id: " + id));
        return mapToResponse(cita);
    }

    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerAgendaDelDia(Long usuarioId, LocalDate fecha) {
        Veterinario veterinario = veterinarioRepository.findByUsuarioId(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
            
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(LocalTime.MAX);
        return citaRepository
            .findByVeterinarioIdAndFechaHoraBetween(veterinario.getId(), inicio, fin)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerAgendaDelMes(Long usuarioId, YearMonth mes) {
        Veterinario veterinario = veterinarioRepository.findByUsuarioId(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
            
        LocalDate primerDia = mes.atDay(1);
        LocalDate ultimoDia = mes.atEndOfMonth();

        LocalDateTime inicio = primerDia.atStartOfDay();
        LocalDateTime fin = ultimoDia.atTime(LocalTime.MAX);

        return citaRepository
            .findByVeterinarioIdAndFechaHoraBetween(veterinario.getId(), inicio, fin)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CitaResponse> obtenerTodasLasCitas(Long usuarioId) {
        Veterinario veterinario = veterinarioRepository.findByUsuarioId(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Veterinario no encontrado"));
            
        return citaRepository
            .findByVeterinarioId(veterinario.getId())
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public CitaResponse pagarCita(Long id) {
        Cita cita = citaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id: " + id));
        cita.setPagado(true);
        Cita guardada = citaRepository.save(cita);
        return mapToResponse(guardada);
    }

    private CitaResponse mapToResponse(Cita cita) {
        CitaResponse dto = new CitaResponse();
        dto.setId(cita.getId());
        dto.setMascotaId(cita.getMascota().getId());
        dto.setMascotaNombre(cita.getMascota().getNombre());
        dto.setClienteId(cita.getMascota().getUsuario().getId());
        dto.setClienteNombre(cita.getMascota().getUsuario().getNombre());
        dto.setVeterinarioId(cita.getVeterinario().getId());
        dto.setVeterinarioNombre(cita.getVeterinario().getUsuario().getNombre());
        dto.setVeterinarioAvatar(cita.getVeterinario().getUsuario().getAvatar());
        dto.setTipoCita(cita.getTipoCita());
        dto.setFechaHora(cita.getFechaHora());
        dto.setDuracionMinutos(cita.getDuracionMinutos());
        dto.setEstado(cita.getEstado());
        dto.setNotas(cita.getNotas());
        dto.setFechaCreacion(cita.getFechaCreacion());
        dto.setPagado(cita.getPagado());
        return dto;
    }
}