package com.team4.petstore.repository;

import com.team4.petstore.entity.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {
    List<Insumo> findByActivoTrue();
    Optional<Insumo> findByIdAndActivoTrue(Long id);
}
