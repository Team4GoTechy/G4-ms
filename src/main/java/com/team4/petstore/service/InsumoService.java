package com.team4.petstore.service;

import com.team4.petstore.dto.request.InsumoRequest;
import com.team4.petstore.dto.response.InsumoResponse;
import com.team4.petstore.entity.Insumo;
import com.team4.petstore.entity.StockInsumo;
import com.team4.petstore.exception.BadRequestException;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.InsumoRepository;
import com.team4.petstore.repository.StockInsumoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InsumoService {

    private final InsumoRepository insumoRepository;
    private final StockInsumoRepository stockInsumoRepository;

    public InsumoService(InsumoRepository insumoRepository, StockInsumoRepository stockInsumoRepository) {
        this.insumoRepository = insumoRepository;
        this.stockInsumoRepository = stockInsumoRepository;
    }

    public List<InsumoResponse> listar() {
        return insumoRepository.findByActivoTrue().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public InsumoResponse obtenerPorId(@NonNull Long id) {
        Insumo insumo = insumoRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id: " + id));
        return toResponse(insumo);
    }

    @Transactional
    public InsumoResponse crear(@NonNull InsumoRequest request) {
        Insumo insumo = new Insumo();
        insumo.setNombre(request.getNombre());
        insumo.setDescripcion(request.getDescripcion());
        insumo.setUnidadMedida(request.getUnidadMedida());
        insumo.setPrecioUnitario(request.getPrecioUnitario());
        insumo.setStockMinimo(request.getStockMinimo() != null ? request.getStockMinimo() : 0);
        insumo.setActivo(true);
        insumo = insumoRepository.save(insumo);

        StockInsumo stock = new StockInsumo(insumo);
        stockInsumoRepository.save(stock);

        return toResponse(insumo);
    }

    @Transactional
    public InsumoResponse actualizar(@NonNull Long id, @NonNull InsumoRequest request) {
        Insumo insumo = insumoRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id: " + id));
        insumo.setNombre(request.getNombre());
        insumo.setDescripcion(request.getDescripcion());
        insumo.setUnidadMedida(request.getUnidadMedida());
        if (request.getPrecioUnitario() != null) {
            insumo.setPrecioUnitario(request.getPrecioUnitario());
        }
        if (request.getStockMinimo() != null) {
            insumo.setStockMinimo(request.getStockMinimo());
        }
        insumo = insumoRepository.save(insumo);
        return toResponse(insumo);
    }

    @Transactional
    public void eliminar(@NonNull Long id) {
        Insumo insumo = insumoRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id: " + id));
        insumo.setActivo(false);
        insumoRepository.save(insumo);
    }

    private InsumoResponse toResponse(Insumo insumo) {
        InsumoResponse response = new InsumoResponse();
        response.setId(insumo.getId());
        response.setNombre(insumo.getNombre());
        response.setDescripcion(insumo.getDescripcion());
        response.setUnidadMedida(insumo.getUnidadMedida());
        response.setPrecioUnitario(insumo.getPrecioUnitario());
        response.setStockMinimo(insumo.getStockMinimo());
        response.setActivo(insumo.getActivo());
        response.setFechaCreacion(insumo.getFechaCreacion());
        return response;
    }
}
