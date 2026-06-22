package com.team4.petstore.service;

import com.team4.petstore.dto.response.NotificacionResponse;
import com.team4.petstore.entity.Notificacion;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.NotificacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Transactional
    public NotificacionResponse crearNotificacion(Usuario usuario, String titulo, String mensaje, String tipo) {
        Notificacion notif = new Notificacion(usuario, titulo, mensaje, tipo);
        return mapToResponse(notificacionRepository.save(notif));
    }

    @Transactional(readOnly = true)
    public List<NotificacionResponse> listarPorUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public NotificacionResponse marcarLeido(Long notificacionId) {
        Notificacion notif = notificacionRepository.findById(notificacionId)
            .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada con id: " + notificacionId));
        notif.setLeido(true);
        return mapToResponse(notificacionRepository.save(notif));
    }

    @Transactional(readOnly = true)
    public long cantidadSinLeer(Long usuarioId) {
        return notificacionRepository.countByUsuarioIdAndLeidoFalse(usuarioId);
    }

    private NotificacionResponse mapToResponse(Notificacion n) {
        return new NotificacionResponse(
            n.getId(),
            n.getTitulo(),
            n.getMensaje(),
            n.getTipo(),
            n.getLeido(),
            n.getFechaCreacion()
        );
    }
}
