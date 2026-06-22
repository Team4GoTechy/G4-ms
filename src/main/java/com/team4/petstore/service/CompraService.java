package com.team4.petstore.service;

import com.team4.petstore.dto.request.CompraRequest;
import com.team4.petstore.dto.response.CompraResponse;
import com.team4.petstore.entity.*;
import com.team4.petstore.exception.BadRequestException;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.exception.StockInsuficienteException;
import com.team4.petstore.repository.CompraRepository;
import com.team4.petstore.repository.ProductoRepository;
import com.team4.petstore.repository.UsuarioRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public CompraService(CompraRepository compraRepository,
                         ProductoRepository productoRepository,
                         UsuarioRepository usuarioRepository) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public CompraResponse crearCompra(@NonNull Long usuarioId, CompraRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        List<DetalleCompra> detalles = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CompraRequest.ItemCompraRequest item : request.getProductos()) {
            Producto producto = productoRepository.findByIdAndActivoTrue(item.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + item.getProductoId()));

            if (producto.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException(
                    "Stock insuficiente para producto: " + producto.getNombre() +
                    ". Disponible: " + producto.getStock() + ", Solicitado: " + item.getCantidad()
                );
            }

            int updated = productoRepository.descontarStock(producto.getId(), item.getCantidad());
            if (updated == 0) {
                throw new StockInsuficienteException(
                    "No se pudo descontar stock para producto: " + producto.getNombre()
                );
            }

            DetalleCompra detalle = new DetalleCompra(producto, item.getCantidad(), producto.getPrecio());
            detalles.add(detalle);

            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        MetodoPago metodoPago;
        try {
            metodoPago = MetodoPago.valueOf(request.getMetodoPago().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Método de pago inválido: " + request.getMetodoPago());
        }

        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.setTotal(total);
        compra.setMetodoPago(metodoPago);
        compra.setEstado(EstadoCompra.PENDIENTE);

        for (DetalleCompra detalle : detalles) {
            compra.addDetalle(detalle);
        }

        compra = compraRepository.save(compra);
        return toResponse(compra);
    }

    public List<CompraResponse> obtenerHistorial(Long usuarioId) {
        List<Compra> compras = compraRepository.findByUsuarioIdWithDetalles(usuarioId);
        return compras.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<CompraResponse> obtenerHistorialPorUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId);
        }
        List<Compra> compras = compraRepository.findByUsuarioIdOrderByFechaDesc(usuarioId);
        return compras.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public CompraResponse updateEstado(Long compraId, String nuevoEstado) {
        Compra compra = compraRepository.findByIdWithDetalles(compraId)
            .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con id: " + compraId));

        EstadoCompra estadoActual = compra.getEstado();
        EstadoCompra nuevo;

        try {
            nuevo = EstadoCompra.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado inválido: " + nuevoEstado);
        }

        if (estadoActual == nuevo) {
            return toResponse(compra);
        }

        if (estadoActual == EstadoCompra.ENTREGADO) {
            throw new BadRequestException("No se puede cambiar el estado de una compra ya entregada");
        }

        boolean restaurarStock = false;
        boolean descontarStock = false;

        if (nuevo == EstadoCompra.CANCELADO && estadoActual != EstadoCompra.CANCELADO) {
            restaurarStock = true;
        } else if (estadoActual == EstadoCompra.CANCELADO && nuevo != EstadoCompra.CANCELADO) {
            descontarStock = true;
        }

        if (restaurarStock) {
            for (DetalleCompra detalle : compra.getDetalles()) {
                productoRepository.restituirStock(detalle.getProducto().getId(), detalle.getCantidad());
            }
        }

        if (descontarStock) {
            for (DetalleCompra detalle : compra.getDetalles()) {
                int updated = productoRepository.descontarStock(detalle.getProducto().getId(), detalle.getCantidad());
                if (updated == 0) {
                    throw new StockInsuficienteException(
                        "Stock insuficiente para restaurar en producto: " + detalle.getProducto().getNombre()
                    );
                }
            }
        }

        compra.setEstado(nuevo);
        compra = compraRepository.save(compra);
        return toResponse(compra);
    }

    private CompraResponse toResponse(Compra compra) {
        CompraResponse response = new CompraResponse();
        response.setId(compra.getId());
        response.setUsuarioId(compra.getUsuario().getId());
        response.setFecha(compra.getFecha());
        response.setTotal(compra.getTotal());
        response.setMetodoPago(compra.getMetodoPago().name());
        response.setEstado(compra.getEstado().name());

        List<CompraResponse.DetalleResponse> detalles = compra.getDetalles().stream()
            .map(d -> new CompraResponse.DetalleResponse(
                d.getProducto().getId(),
                d.getProducto().getNombre(),
                d.getCantidad(),
                d.getPrecioUnitario()
            ))
            .collect(Collectors.toList());
        response.setProductos(detalles);

        return response;
    }
}
