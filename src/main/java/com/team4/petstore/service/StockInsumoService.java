package com.team4.petstore.service;

import com.team4.petstore.dto.request.ConsumoRequest;
import com.team4.petstore.dto.response.MovimientoInsumoResponse;
import com.team4.petstore.dto.response.StockInsumoResponse;
import com.team4.petstore.entity.*;
import com.team4.petstore.exception.BadRequestException;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.InsumoRepository;
import com.team4.petstore.repository.MovimientoInsumoRepository;
import com.team4.petstore.repository.StockInsumoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockInsumoService {

    private final StockInsumoRepository stockInsumoRepository;
    private final InsumoRepository insumoRepository;
    private final MovimientoInsumoRepository movimientoRepository;

    public StockInsumoService(StockInsumoRepository stockInsumoRepository,
                              InsumoRepository insumoRepository,
                              MovimientoInsumoRepository movimientoRepository) {
        this.stockInsumoRepository = stockInsumoRepository;
        this.insumoRepository = insumoRepository;
        this.movimientoRepository = movimientoRepository;
    }

    public List<StockInsumoResponse> listarStock() {
        return stockInsumoRepository.findAllWithInsumoActivo().stream()
            .map(this::toStockResponse)
            .collect(Collectors.toList());
    }

    public StockInsumoResponse obtenerStockPorInsumoId(Long insumoId) {
        StockInsumo stock = stockInsumoRepository.findByInsumoId(insumoId)
            .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado para insumo id: " + insumoId));
        return toStockResponse(stock);
    }

    public List<MovimientoInsumoResponse> obtenerHistorial(Long insumoId) {
        return movimientoRepository.findByInsumoIdOrderByFechaDesc(insumoId).stream()
            .map(this::toMovimientoResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void registrarConsumo(ConsumoRequest request) {
        for (ConsumoRequest.ConsumoItem item : request.getItems()) {
            Insumo insumo = insumoRepository.findByIdAndActivoTrue(item.getInsumoId())
                .orElseThrow(() -> new ResourceNotFoundException("Insumo no encontrado con id: " + item.getInsumoId()));

            StockInsumo stock = stockInsumoRepository.findByInsumoId(item.getInsumoId())
                .orElseThrow(() -> new ResourceNotFoundException("Stock no encontrado para insumo id: " + item.getInsumoId()));

            if (stock.getCantidadActual() < item.getCantidad()) {
                throw new BadRequestException("Stock insuficiente para insumo: " + insumo.getNombre() +
                    ". Disponible: " + stock.getCantidadActual() + ", Solicitado: " + item.getCantidad());
            }

            stock.setCantidadActual(stock.getCantidadActual() - item.getCantidad());
            stockInsumoRepository.save(stock);

            MovimientoInsumo movimiento = new MovimientoInsumo();
            movimiento.setInsumo(insumo);
            movimiento.setTipo(TipoMovimiento.SALIDA);
            movimiento.setCantidad(item.getCantidad());
            movimiento.setPrecioUnitario(insumo.getPrecioUnitario());
            movimiento.setDescripcion(request.getDescripcion());
            movimientoRepository.save(movimiento);
        }
    }

    public List<StockInsumoResponse> listarStockDisponibles() {
        return listarStock();
    }

    private StockInsumoResponse toStockResponse(StockInsumo stock) {
        StockInsumoResponse response = new StockInsumoResponse();
        response.setInsumoId(stock.getInsumo().getId());
        response.setNombreInsumo(stock.getInsumo().getNombre());
        response.setCantidadActual(stock.getCantidadActual());
        response.setStockMinimo(stock.getInsumo().getStockMinimo());
        response.setAlertaStock(stock.getCantidadActual() <= stock.getInsumo().getStockMinimo());
        response.setUnidadMedida(stock.getInsumo().getUnidadMedida());
        response.setPrecioUnitario(stock.getInsumo().getPrecioUnitario());
        return response;
    }

    private MovimientoInsumoResponse toMovimientoResponse(MovimientoInsumo movimiento) {
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
