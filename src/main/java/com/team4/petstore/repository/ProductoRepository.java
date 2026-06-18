package com.team4.petstore.repository;

import com.team4.petstore.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    Optional<Producto> findByIdAndActivoTrue(Long id);
    Optional<Producto> findByCodigo(String codigo);
    List<Producto> findByCategoriaIdAndActivoTrue(Long categoriaId);
    boolean existsByCodigo(String codigo);

    @Modifying
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad WHERE p.id = :id AND p.stock >= :cantidad")
    int descontarStock(@Param("id") Long id, @Param("cantidad") Integer cantidad);
}
