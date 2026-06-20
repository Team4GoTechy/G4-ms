package com.team4.petstore.repository;

import com.team4.petstore.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    List<Proveedor> findByActivoTrue();
    Optional<Proveedor> findByIdAndActivoTrue(Long id);
}
