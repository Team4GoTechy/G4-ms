package com.team4.petstore.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;
import com.team4.petstore.service.ProductoService;
import com.team4.petstore.entity.Producto;
import org.springframework.web.bind.annotation.RequestBody;


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

}
