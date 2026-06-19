package com.team4.petstore.repository;

import com.team4.petstore.entity.MovimientoInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MovimientoInsumoRepository extends JpaRepository<MovimientoInsumo, Long> {
    @Query("SELECT m FROM MovimientoInsumo m JOIN FETCH m.insumo WHERE m.insumo.id = :insumoId ORDER BY m.fecha DESC")
    List<MovimientoInsumo> findByInsumoIdOrderByFechaDesc(@Param("insumoId") Long insumoId);
}
