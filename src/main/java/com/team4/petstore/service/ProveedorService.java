package com.team4.petstore.service;

import com.team4.petstore.dto.request.ProveedorRequest;
import com.team4.petstore.dto.response.ProveedorResponse;
import com.team4.petstore.entity.Proveedor;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.ProveedorRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<ProveedorResponse> listar() {
        return proveedorRepository.findByActivoTrue().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public ProveedorResponse obtenerPorId(@NonNull Long id) {
        Proveedor proveedor = proveedorRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        return toResponse(proveedor);
    }

    @Transactional
    public ProveedorResponse crear(@NonNull ProveedorRequest request) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(request.getNombre());
        proveedor.setEmail(request.getEmail());
        proveedor.setTelefono(request.getTelefono());
        proveedor.setActivo(true);
        proveedor = proveedorRepository.save(proveedor);
        return toResponse(proveedor);
    }

    @Transactional
    public ProveedorResponse actualizar(@NonNull Long id, @NonNull ProveedorRequest request) {
        Proveedor proveedor = proveedorRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        proveedor.setNombre(request.getNombre());
        proveedor.setEmail(request.getEmail());
        proveedor.setTelefono(request.getTelefono());
        proveedor = proveedorRepository.save(proveedor);
        return toResponse(proveedor);
    }

    @Transactional
    public void eliminar(@NonNull Long id) {
        Proveedor proveedor = proveedorRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado con id: " + id));
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    private ProveedorResponse toResponse(Proveedor proveedor) {
        ProveedorResponse response = new ProveedorResponse();
        response.setId(proveedor.getId());
        response.setNombre(proveedor.getNombre());
        response.setEmail(proveedor.getEmail());
        response.setTelefono(proveedor.getTelefono());
        response.setActivo(proveedor.getActivo());
        response.setFechaCreacion(proveedor.getFechaCreacion());
        return response;
    }
}
