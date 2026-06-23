package com.team4.petstore.service;

import com.team4.petstore.dto.request.DetallePrescripcionRequest;
import com.team4.petstore.dto.request.PrescripcionRequest;
import com.team4.petstore.dto.response.DetallePrescripcionResponse;
import com.team4.petstore.dto.response.PrescripcionResponse;
import com.team4.petstore.entity.Consulta;
import com.team4.petstore.entity.DetallePrescripcion;
import com.team4.petstore.entity.Prescripcion;
import com.team4.petstore.entity.Insumo;
import com.team4.petstore.entity.Veterinario;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.entity.enums.TipoNotificacion;
import com.team4.petstore.repository.ConsultaRepository;
import com.team4.petstore.repository.PrescripcionRepository;
import com.team4.petstore.repository.InsumoRepository;
import com.team4.petstore.repository.UsuarioRepository;
import com.team4.petstore.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrescripcionService {

    private final PrescripcionRepository prescripcionRepository;
    private final ConsultaRepository consultaRepository;
    private final VeterinarioRepository veterinarioRepository;
    private final InsumoRepository insumoRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionService notificacionService;

    public PrescripcionService(PrescripcionRepository prescripcionRepository,
                               ConsultaRepository consultaRepository,
                               VeterinarioRepository veterinarioRepository,
                               InsumoRepository insumoRepository,
                               UsuarioRepository usuarioRepository,
                               NotificacionService notificacionService) {
        this.prescripcionRepository = prescripcionRepository;
        this.consultaRepository = consultaRepository;
        this.veterinarioRepository = veterinarioRepository;
        this.insumoRepository = insumoRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionService = notificacionService;
    }

    // email viene del token JWT via @AuthenticationPrincipal en el controller
    @Transactional
    public PrescripcionResponse crear(PrescripcionRequest dto, String emailVeterinario) {
        Consulta consulta = consultaRepository.findById(dto.getConsultaId())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Consulta no encontrada con id: " + dto.getConsultaId()));

        // Buscamos el Veterinario a través del Usuario cuyo email conocemos
        Veterinario veterinario = usuarioRepository.findByEmail(emailVeterinario)
            .flatMap(u -> veterinarioRepository.findByUsuarioId(u.getId()))
            .orElseThrow(() -> new ResourceNotFoundException(
                "Veterinario no encontrado para el usuario: " + emailVeterinario));

        Prescripcion prescripcion = new Prescripcion();
        prescripcion.setConsulta(consulta);
        prescripcion.setVeterinario(veterinario);
        prescripcion.setObservaciones(dto.getObservaciones());

        List<DetallePrescripcion> detalles = new ArrayList<>();
        for (DetallePrescripcionRequest detalleDto : dto.getDetalles()) {
            Insumo insumo = insumoRepository.findById(detalleDto.getInsumoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Insumo no encontrado con id: " + detalleDto.getInsumoId()));

            DetallePrescripcion detalle = new DetallePrescripcion();
            detalle.setPrescripcion(prescripcion);
            detalle.setInsumo(insumo);
            detalle.setDosis(detalleDto.getDosis());
            detalle.setFrecuencia(detalleDto.getFrecuencia());
            detalle.setDuracion(detalleDto.getDuracion());
            detalle.setViaAdministracion(detalleDto.getViaAdministracion());
            detalle.setInstrucciones(detalleDto.getInstrucciones());
            detalles.add(detalle);
        }

        prescripcion.setDetalles(detalles);
        Prescripcion guardada = prescripcionRepository.save(prescripcion);

        try {
            notificacionService.crearNotificacion(
                consulta.getMascota().getUsuario().getId(),
                "Nueva Receta Médica",
                "Se ha emitido una nueva receta médica para tu mascota " + consulta.getMascota().getNombre() + " por el doctor " + veterinario.getUsuario().getNombre() + ".",
                TipoNotificacion.SISTEMA
            );
        } catch (Exception e) {
            System.err.println("Error enviando notificación de receta: " + e.getMessage());
        }

        return mapToResponse(guardada);
    }

    @Transactional(readOnly = true)
    public PrescripcionResponse buscarPorConsulta(Long consultaId) {
        Prescripcion prescripcion = prescripcionRepository.findByConsultaId(consultaId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Prescripción no encontrada para la consulta: " + consultaId));
        return mapToResponse(prescripcion);
    }

    @Transactional(readOnly = true)
    public List<PrescripcionResponse> buscarPorMascota(Long mascotaId) {
        List<PrescripcionResponse> result = new ArrayList<>();
        for (Prescripcion p : prescripcionRepository.findByConsultaMascotaId(mascotaId)) {
            result.add(mapToResponse(p));
        }
        return result;
    }

    private PrescripcionResponse mapToResponse(Prescripcion p) {
        PrescripcionResponse dto = new PrescripcionResponse();
        dto.setId(p.getId());
        dto.setConsultaId(p.getConsulta().getId());
        dto.setVeterinarioId(p.getVeterinario().getId());
        dto.setVeterinarioNombre(p.getVeterinario().getUsuario().getNombre());
        dto.setFecha(p.getFecha());
        dto.setObservaciones(p.getObservaciones());

        List<DetallePrescripcionResponse> detallesDto = new ArrayList<>();
        for (DetallePrescripcion d : p.getDetalles()) {
            DetallePrescripcionResponse detalleDto = new DetallePrescripcionResponse();
            detalleDto.setId(d.getId());
            detalleDto.setInsumoId(d.getInsumo().getId());
            detalleDto.setInsumoNombre(d.getInsumo().getNombre());
            detalleDto.setDosis(d.getDosis());
            detalleDto.setFrecuencia(d.getFrecuencia());
            detalleDto.setDuracion(d.getDuracion());
            detalleDto.setViaAdministracion(d.getViaAdministracion());
            detalleDto.setInstrucciones(d.getInstrucciones());
            detallesDto.add(detalleDto);
        }
        dto.setDetalles(detallesDto);
        return dto;
    }
}