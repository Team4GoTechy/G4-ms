package com.team4.petstore.service;

import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import java.util.List;
import com.team4.petstore.entity.Producto;
import com.team4.petstore.repository.ProductoRepository;

@Service
public class ProductoService {

    private ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    public Producto guardar(@NonNull Producto producto) {
        return repository.save(producto);
    }
}
