package com.team4.petstore.service;

import com.team4.petstore.entity.Usuario;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CloudinaryService cloudinaryService;

    public UsuarioService(UsuarioRepository usuarioRepository, CloudinaryService cloudinaryService) {
        this.usuarioRepository = usuarioRepository;
        this.cloudinaryService = cloudinaryService;
    }

    @Transactional
    public String updateAvatar(Long usuarioId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El archivo debe ser una imagen");
        }

        if (file.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("El archivo debe ser menor a 5MB");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        String avatarUrl = cloudinaryService.uploadAvatarImage(file);
        usuario.setAvatar(avatarUrl);
        usuarioRepository.save(usuario);

        return avatarUrl;
    }

    public Usuario getById(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));
    }
}
