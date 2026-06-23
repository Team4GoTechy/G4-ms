package com.team4.petstore.service;

import com.team4.petstore.dto.request.ProductoRequest;
import com.team4.petstore.dto.response.ProductoResponse;
import com.team4.petstore.entity.Categoria;
import com.team4.petstore.entity.Producto;
import com.team4.petstore.entity.UnidadMedida;
import com.team4.petstore.exception.BadRequestException;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.repository.CategoriaRepository;
import com.team4.petstore.repository.ProductoRepository;
import com.team4.petstore.repository.CompraRepository;
import com.team4.petstore.entity.Compra;
import com.team4.petstore.entity.DetalleCompra;
import com.team4.petstore.entity.EstadoCompra;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final CompraRepository compraRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository,
                           CompraRepository compraRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.compraRepository = compraRepository;
    }

    public List<ProductoResponse> listar() {
        return productoRepository.findByActivoTrue().stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public List<ProductoResponse> listarPorCategoria(@NonNull Long categoriaId) {
        return productoRepository.findByCategoriaIdAndActivoTrue(categoriaId).stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public ProductoResponse obtenerPorId(@NonNull Long id) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return toResponse(producto);
    }

    @Transactional
    public ProductoResponse guardar(@NonNull ProductoRequest request) {
        if (productoRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException("El código del producto ya existe: " + request.getCodigo());
        }

        Producto producto = new Producto();
        producto.setNombre(request.getNombre());
        producto.setCodigo(request.getCodigo());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setPesoKg(request.getPesoKg());

        if (request.getUnidadMedida() != null && !request.getUnidadMedida().isBlank()) {
            try {
                UnidadMedida unidad = UnidadMedida.valueOf(request.getUnidadMedida().toUpperCase());
                producto.setUnidadMedida(unidad);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Unidad de medida inválida: " + request.getUnidadMedida());
            }
        }

        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + request.getCategoriaId()));
            producto.setCategoria(categoria);
        }

        producto = productoRepository.save(producto);
        return toResponse(producto);
    }

    @Transactional
    public ProductoResponse actualizar(@NonNull Long id, @NonNull ProductoRequest request) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));

        if (!producto.getCodigo().equals(request.getCodigo()) && productoRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException("El código del producto ya existe: " + request.getCodigo());
        }

        producto.setNombre(request.getNombre());
        producto.setCodigo(request.getCodigo());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setImagenUrl(request.getImagenUrl());
        producto.setPesoKg(request.getPesoKg());

        if (request.getUnidadMedida() != null && !request.getUnidadMedida().isBlank()) {
            try {
                UnidadMedida unidad = UnidadMedida.valueOf(request.getUnidadMedida().toUpperCase());
                producto.setUnidadMedida(unidad);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Unidad de medida inválida: " + request.getUnidadMedida());
            }
        } else {
            producto.setUnidadMedida(null);
        }

        if (request.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con id: " + request.getCategoriaId()));
            producto.setCategoria(categoria);
        } else {
            producto.setCategoria(null);
        }

        producto = productoRepository.save(producto);
        return toResponse(producto);
    }

    @Transactional
    public void eliminar(@NonNull Long id) {
        Producto producto = productoRepository.findByIdAndActivoTrue(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        producto.setActivo(false);
        productoRepository.save(producto);

        // Cancel pending purchases containing this product and restore their stock
        List<Compra> pendingCompras = compraRepository.findPendingComprasByProductoId(id);
        for (Compra compra : pendingCompras) {
            for (DetalleCompra detalle : compra.getDetalles()) {
                productoRepository.restituirStock(detalle.getProducto().getId(), detalle.getCantidad());
            }
            compra.setEstado(EstadoCompra.CANCELADO);
            compraRepository.save(compra);
        }
    }

    private ProductoResponse toResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setCodigo(producto.getCodigo());
        response.setDescripcion(producto.getDescripcion());
        response.setPrecio(producto.getPrecio());
        response.setStock(producto.getStock());
        if (producto.getUnidadMedida() != null) {
            response.setUnidadMedida(producto.getUnidadMedida().getSimbolo());
        }
        response.setActivo(producto.getActivo());
        response.setImagenUrl(producto.getImagenUrl());
        response.setPesoKg(producto.getPesoKg());
        if (producto.getCategoria() != null) {
            response.setCategoria(producto.getCategoria().getNombre());
        }
        return response;
    }
}
