package com.team4.petstore.service;

import com.team4.petstore.dto.response.MovimientoInsumoResponse;
import com.team4.petstore.entity.MovimientoInsumo;
import com.team4.petstore.entity.TipoMovimiento;
import com.team4.petstore.repository.InsumoRepository;
import com.team4.petstore.repository.MovimientoInsumoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoInsumoService {

    private final MovimientoInsumoRepository movimientoRepository;
    private final InsumoRepository insumoRepository;

    public MovimientoInsumoService(MovimientoInsumoRepository movimientoRepository,
                                  InsumoRepository insumoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.insumoRepository = insumoRepository;
    }

    @Transactional
    public void registrarEntrada(Long insumoId, Integer cantidad, BigDecimal precioUnitario, String descripcion, Long referenciaId) {
        MovimientoInsumo movimiento = new MovimientoInsumo();
        movimiento.setInsumo(insumoRepository.findById(insumoId)
            .orElseThrow(() -> new RuntimeException("Insumo no encontrado")));
        movimiento.setTipo(TipoMovimiento.ENTRADA);
        movimiento.setCantidad(cantidad);
        movimiento.setPrecioUnitario(precioUnitario);
        movimiento.setDescripcion(descripcion);
        movimiento.setReferenciaId(referenciaId);
        movimientoRepository.save(movimiento);
    }

    public List<MovimientoInsumoResponse> listarPorInsumo(Long insumoId) {
        return movimientoRepository.findByInsumoIdOrderByFechaDesc(insumoId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    private MovimientoInsumoResponse toResponse(MovimientoInsumo movimiento) {
        MovimientoInsumoResponse response = new MovimientoInsumoResponse();
        response.setId(movimiento.getId());
        response.setInsumoId(movimiento.getInsumo().getId());
        response.setNombreInsumo(movimiento.getInsumo().getNombre());
        response.setTipo(movimiento.getTipo().name());
        response.setCantidad(movimiento.getCantidad());
        response.setPrecioUnitario(movimiento.getPrecioUnitario());
        response.setFecha(movimiento.getFecha());
        response.setDescripcion(movimiento.getDescripcion());
        response.setReferenciaId(movimiento.getReferenciaId());
        return response;
    }
}
