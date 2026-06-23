package com.team4.petstore.service;

import com.team4.petstore.entity.Notificacion;
import com.team4.petstore.entity.Usuario;
import com.team4.petstore.entity.enums.TipoNotificacion;
import com.team4.petstore.exception.BadRequestException;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.NotificacionRepository;
import com.team4.petstore.repository.UsuarioRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;

    public NotificacionService(NotificacionRepository notificacionRepository, UsuarioRepository usuarioRepository) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Notificacion crearNotificacion(@NonNull Long usuarioId, @NonNull String titulo, @NonNull String mensaje, @NonNull TipoNotificacion tipo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        Notificacion notificacion = new Notificacion(usuario, titulo, mensaje, tipo);
        return notificacionRepository.save(notificacion);
    }

    @Transactional(readOnly = true)
    public List<Notificacion> listarPorUsuario(@NonNull Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Notificacion> listarNoLeidasPorUsuario(@NonNull Long usuarioId) {
        return notificacionRepository.findByUsuarioIdAndLeidoFalseOrderByFechaCreacionDesc(usuarioId);
    }

    @Transactional(readOnly = true)
    public Long cantidadSinLeer(@NonNull Long usuarioId) {
        return notificacionRepository.countSinLeer(usuarioId);
    }

    @Transactional
    public Notificacion marcarLeido(@NonNull Long notificacionId, @NonNull Long usuarioId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
            .orElseThrow(() -> new ResourceNotFoundException("Notificacion no encontrada con id: " + notificacionId));

        if (!notificacion.getUsuario().getId().equals(usuarioId)) {
            throw new BadRequestException("La notificación no pertenece al usuario");
        }

        notificacion.setLeido(true);
        return notificacionRepository.save(notificacion);
    }

    @Transactional
    public void marcarTodasLeidas(@NonNull Long usuarioId) {
        List<Notificacion> noLeidas = notificacionRepository.findByUsuarioIdAndLeidoFalseOrderByFechaCreacionDesc(usuarioId);
        for (Notificacion n : noLeidas) {
            n.setLeido(true);
        }
        notificacionRepository.saveAll(noLeidas);
    }
}
