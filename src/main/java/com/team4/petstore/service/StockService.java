package com.team4.petstore.service;

import com.team4.petstore.entity.Producto;
import com.team4.petstore.exception.ResourceNotFoundException;
import com.team4.petstore.exception.StockInsuficienteException;
import com.team4.petstore.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private final ProductoRepository productoRepository;

    public StockService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public void descontar(Long productoId, Integer cantidad) {
        Producto producto = productoRepository.findById(productoId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Producto no encontrado con id: " + productoId));

        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException(
                "Stock insuficiente para el producto: " + producto.getNombre() +
                ". Stock actual: " + producto.getStock() + ", requerido: " + cantidad);
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);
    }
}