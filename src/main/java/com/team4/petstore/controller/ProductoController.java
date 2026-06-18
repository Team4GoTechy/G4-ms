package com.team4.petstore.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;
import java.util.Optional;
import com.team4.petstore.service.ProductoService;
import com.team4.petstore.entity.Producto;


@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Producto> obtenerProductos() {
        return service.listar();
    }

    @PostMapping
    public Producto crearProducto(@RequestBody @NonNull Producto producto) {
        return service.guardar(producto);
    }

    @PutMapping("/{id}")
    public Optional<Producto> actualizarProducto(@PathVariable @NonNull Long id, @RequestBody @NonNull Producto producto) {
        return service.actualizar(id, producto);
    }

    @DeleteMapping("/{id}")
    public boolean eliminarProducto(@PathVariable @NonNull Long id) {
        return service.eliminar(id);
    }

}
