package com.team4.petstore.service;

import com.team4.petstore.dto.request.EvolucionRequest;
import com.team4.petstore.dto.request.InternacionRequest;
import com.team4.petstore.dto.request.ReingresoRequest;
import com.team4.petstore.dto.response.EvolucionResponse;
import com.team4.petstore.dto.response.InternacionResponse;
import com.team4.petstore.entity.*;
import com.team4.petstore.entity.enums.EstadoInternacion;
import com.team4.petstore.entity.enums.TipoNotificacion;
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
    private final NotificacionService notificacionService;

    public InternacionService(InternacionRepository internacionRepository,
                              EvolucionInternacionRepository evolucionRepository,
                              MascotaRepository mascotaRepository,
                              VeterinarioRepository veterinarioRepository,
                              UsuarioRepository usuarioRepository,
                              NotificacionService notificacionService) {
        this.internacionRepository = internacionRepository;
        this.evolucionRepository = evolucionRepository;
        this.mascotaRepository = mascotaRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public InternacionResponse ingresar(InternacionRequest dto) {
        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Mascota no encontrada con id: " + dto.getMascotaId()));

        Veterinario veterinario = veterinarioRepository.findByUsuarioId(dto.getVeterinarioId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Veterinario no encontrado para usuario id: " + dto.getVeterinarioId()));

        Internacion internacion = new Internacion();
        internacion.setMascota(mascota);
        internacion.setVeterinario(veterinario);
        internacion.setMotivo(dto.getMotivo());
        internacion.setJaulaId(dto.getJaulaId());
        internacion.setNotas(dto.getNotas());
        internacion.setEstado(EstadoInternacion.ACTIVA);

        Internacion guardada = internacionRepository.save(internacion);

        try {
            notificacionService.crearNotificacion(
                mascota.getUsuario().getId(),
                "Ingreso a Internación",
                "Tu mascota " + mascota.getNombre() + " ha ingresado a internación. Motivo: " + dto.getMotivo() + ".",
                TipoNotificacion.SISTEMA
            );
        } catch (Exception e) {
            System.err.println("Error al notificar ingreso a internación: " + e.getMessage());
        }

        return mapToResponse(guardada);
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
    public InternacionResponse darDeAlta(Long internacionId, String indicacionesAlta) {
        Internacion internacion = internacionRepository.findById(internacionId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Internación no encontrada con id: " + internacionId));

        internacion.setEstado(EstadoInternacion.ALTA);
        internacion.setFechaAlta(LocalDateTime.now());
        internacion.setIndicacionesAlta(indicacionesAlta);

        Internacion guardada = internacionRepository.save(internacion);

        try {
            notificacionService.crearNotificacion(
                guardada.getMascota().getUsuario().getId(),
                "Alta de Internación",
                "¡Buenas noticias! Tu mascota " + guardada.getMascota().getNombre() + " ha sido dada de alta. Indicaciones: " + indicacionesAlta,
                TipoNotificacion.SISTEMA
            );
        } catch (Exception e) {
            System.err.println("Error al notificar alta de internación: " + e.getMessage());
        }

        return mapToResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<InternacionResponse> listarActivas() {
        List<InternacionResponse> resultado = new ArrayList<>();
        for (Internacion i : internacionRepository.findByEstado(EstadoInternacion.ACTIVA)) {
            resultado.add(mapToResponse(i));
        }
        return resultado;
    }

    @Transactional(readOnly = true)
    public List<InternacionResponse> listarPendientesReingreso() {
        List<InternacionResponse> resultado = new ArrayList<>();
        for (Internacion i : internacionRepository.findByEstado(EstadoInternacion.REINGRESO_SOLICITADO)) {
            resultado.add(mapToResponse(i));
        }
        return resultado;
    }

    @Transactional(readOnly = true)
    public List<InternacionResponse> obtenerPorMascota(Long mascotaId) {
        List<InternacionResponse> resultado = new ArrayList<>();
        for (Internacion i : internacionRepository.findByMascotaId(mascotaId)) {
            resultado.add(mapToResponse(i));
        }
        return resultado;
    }

    @Transactional
    public InternacionResponse solicitarReingreso(ReingresoRequest dto, String emailCliente) {
        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Mascota no encontrada con id: " + dto.getMascotaId()));

        Usuario cliente = usuarioRepository.findByEmail(emailCliente)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        if (!mascota.getUsuario().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("La mascota no pertenece al usuario autenticado");
        }

        Veterinario veterinario = null;
        List<Internacion> previous = internacionRepository.findByMascotaId(mascota.getId());
        if (!previous.isEmpty()) {
            veterinario = previous.get(previous.size() - 1).getVeterinario();
        } else {
            List<Veterinario> vets = veterinarioRepository.findAll();
            if (!vets.isEmpty()) {
                veterinario = vets.get(0);
            } else {
                throw new ResourceNotFoundException("No hay veterinarios registrados en el sistema");
            }
        }

        Internacion internacion = new Internacion();
        internacion.setMascota(mascota);
        internacion.setVeterinario(veterinario);
        internacion.setNotasCliente(dto.getNotasCliente());
        internacion.setMotivo("Reingreso Solicitado por Propietario: " + dto.getNotasCliente());
        internacion.setEstado(EstadoInternacion.REINGRESO_SOLICITADO);
        internacion.setFechaIngreso(LocalDateTime.now());

        return mapToResponse(internacionRepository.save(internacion));
    }

    @Transactional
    public InternacionResponse confirmarReingreso(Long id, String jaulaId) {
        Internacion internacion = internacionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Internación no encontrada con id: " + id));

        if (internacion.getEstado() != EstadoInternacion.REINGRESO_SOLICITADO) {
            throw new IllegalStateException("La internación no está en estado de reingreso solicitado");
        }

        internacion.setEstado(EstadoInternacion.ACTIVA);
        internacion.setJaulaId(jaulaId);
        internacion.setFechaIngreso(LocalDateTime.now());

        Internacion guardada = internacionRepository.save(internacion);

        try {
            notificacionService.crearNotificacion(
                guardada.getMascota().getUsuario().getId(),
                "Reingreso Confirmado",
                "El reingreso solicitado para tu mascota " + guardada.getMascota().getNombre() + " ha sido confirmado en la jaula " + jaulaId + ".",
                TipoNotificacion.SISTEMA
            );
        } catch (Exception e) {
            System.err.println("Error al notificar confirmación de reingreso: " + e.getMessage());
        }

        return mapToResponse(guardada);
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
        dto.setIndicacionesAlta(i.getIndicacionesAlta());
        dto.setNotasCliente(i.getNotasCliente());

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