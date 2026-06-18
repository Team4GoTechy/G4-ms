package com.team4.petstore.service;

import com.team4.petstore.entity.Categoria;
import com.team4.petstore.exception.BadRequestException;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.CategoriaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria obtenerPorId(@NonNull Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
    }

    @Transactional
    public Categoria guardar(@NonNull String nombre, String descripcion) {
        if (categoriaRepository.existsByNombre(nombre)) {
            throw new BadRequestException("La categoría ya existe: " + nombre);
        }
        Categoria categoria = new Categoria(nombre, descripcion);
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void eliminar(@NonNull Long id) {
        Categoria categoria = categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + id));
        categoriaRepository.delete(categoria);
    }
}
