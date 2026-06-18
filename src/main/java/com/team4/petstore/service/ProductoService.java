package com.team4.petstore.service;

import org.springframework.stereotype.Service;
import org.springframework.lang.NonNull;
import java.util.List;
import java.util.Optional;
import com.team4.petstore.entity.Producto;
import com.team4.petstore.repository.ProductoRepository;

@Service
public class ProductoService {

    private ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listar() {
        return repository.findByActivoTrue();
    }

    public Producto guardar(@NonNull Producto producto) {
        return repository.save(producto);
    }

    public Optional<Producto> actualizar(@NonNull Long id, @NonNull Producto producto) {
        Optional<Producto> productoExistente = repository.findByIdAndActivoTrue(id);
        
        if (productoExistente.isPresent()) {
            Producto p = productoExistente.get();
            p.setNombre(producto.getNombre());
            p.setCodigo(producto.getCodigo());
            p.setDescripcion(producto.getDescripcion());
            p.setPrecio(producto.getPrecio());
            p.setStock(producto.getStock());
            return Optional.of(repository.save(p));
        }
        
        return Optional.empty();
    }

    public boolean eliminar(@NonNull Long id) {
        Optional<Producto> producto = repository.findByIdAndActivoTrue(id);
        
        if (producto.isPresent()) {
            Producto p = producto.get();
            p.setActivo(false);
            repository.save(p);
            return true;
        }
        
        return false;
    }
}
