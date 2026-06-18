package com.team4.petstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.team4.petstore.entity.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    List<Producto> findByActivoTrue();
    
    Optional<Producto> findByIdAndActivoTrue(Long id);
}
