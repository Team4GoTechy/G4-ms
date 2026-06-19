package com.team4.petstore.service;

import com.team4.petstore.dto.request.OrdenCompraCompletarRequest;
import com.team4.petstore.dto.response.OrdenCompraResponse;
import com.team4.petstore.entity.*;
import com.team4.petstore.exception.BadRequestException;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdenCompraService {

    private final OrdenCompraRepository ordenRepository;
    private final InsumoRepository insumoRepository;
    private final StockInsumoRepository stockRepository;
    private final MovimientoInsumoRepository movimientoRepository;
    private final MovimientoInsumoService movimientoService;

    public OrdenCompraService(OrdenCompraRepository ordenRepository,
                             InsumoRepository insumoRepository,
                             StockInsumoRepository stockRepository,
                             MovimientoInsumoRepository movimientoRepository,
                             MovimientoInsumoService movimientoService) {
        this.ordenRepository = ordenRepository;
        this.insumoRepository = insumoRepository;
        this.stockRepository = stockRepository;
        this.movimientoRepository = movimientoRepository;
        this.movimientoService = movimientoService;
    }

    public List<OrdenCompraResponse> listar() {
        return ordenRepository.findAllWithDetallesAndProveedor().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public OrdenCompraResponse obtenerPorId(@NonNull Long id) {
        OrdenCompra orden = ordenRepository.findByIdWithDetalles(id)
            .orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada con id: " + id));
        return toResponse(orden);
    }

    @Transactional
    public OrdenCompraResponse completar(@NonNull Long ordenId, @NonNull OrdenCompraCompletarRequest request) {
        OrdenCompra orden = ordenRepository.findByIdWithDetalles(ordenId)
            .orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada con id: " + ordenId));

        if (orden.getEstado() != EstadoOrdenCompra.PENDIENTE) {
            throw new BadRequestException("La orden ya no esta en estado PENDIENTE");
        }

        for (OrdenCompraCompletarRequest.OrdenItem item : request.getItems()) {
            Insumo insumo = insumoRepository.findByIdAndActivoTrue(item.getInsumoId())
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id: " + item.getInsumoId()));

            insumo.setPrecioUnitario(item.getPrecioUnitario());
            insumoRepository.save(insumo);

            StockInsumo stock = stockRepository.findByInsumoId(item.getInsumoId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado para insumo id: " + item.getInsumoId()));

            stock.setCantidadActual(stock.getCantidadActual() + item.getCantidad());
            stockRepository.save(stock);

            MovimientoInsumo movimiento = new MovimientoInsumo();
            movimiento.setInsumo(insumo);
            movimiento.setTipo(TipoMovimiento.ENTRADA);
            movimiento.setCantidad(item.getCantidad());
            movimiento.setPrecioUnitario(item.getPrecioUnitario());
            movimiento.setDescripcion("Entrada por orden de compra #" + orden.getId() + " - Proveedor: " + orden.getProveedor().getNombre());
            movimiento.setReferenciaId(orden.getId());
            movimientoRepository.save(movimiento);
        }

        orden.setEstado(EstadoOrdenCompra.COMPLETADA);
        orden = ordenRepository.save(orden);

        return toResponse(orden);
    }

    @Transactional
    public OrdenCompraResponse cancelar(@NonNull Long ordenId) {
        OrdenCompra orden = ordenRepository.findByIdWithProveedor(ordenId)
            .orElseThrow(() -> new ResourceNotFoundException("Orden de compra no encontrada con id: " + ordenId));

        if (orden.getEstado() != EstadoOrdenCompra.PENDIENTE) {
            throw new BadRequestException("La orden ya no esta en estado PENDIENTE");
        }

        orden.setEstado(EstadoOrdenCompra.CANCELADA);
        orden = ordenRepository.save(orden);

        return toResponse(orden);
    }

    private OrdenCompraResponse toResponse(OrdenCompra orden) {
        OrdenCompraResponse response = new OrdenCompraResponse();
        response.setId(orden.getId());
        response.setProveedorId(orden.getProveedor().getId());
        response.setNombreProveedor(orden.getProveedor().getNombre());
        response.setEstado(orden.getEstado().name());
        response.setFechaCreacion(orden.getFechaCreacion());

        List<OrdenCompraResponse.DetalleResponse> detalles = orden.getDetalles().stream()
            .map(d -> new OrdenCompraResponse.DetalleResponse(
                d.getInsumo().getId(),
                d.getInsumo().getNombre(),
                d.getCantidad(),
                d.getPrecioUnitario()
            ))
            .collect(Collectors.toList());
        response.setDetalles(detalles);

        BigDecimal total = detalles.stream()
            .map(d -> d.getSubtotal())
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotal(total);

        return response;
    }
}
