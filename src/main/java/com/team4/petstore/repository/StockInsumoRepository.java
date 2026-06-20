package com.team4.petstore.repository;

import com.team4.petstore.entity.StockInsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface StockInsumoRepository extends JpaRepository<StockInsumo, Long> {
    @Query("SELECT s FROM StockInsumo s JOIN FETCH s.insumo WHERE s.insumo.id = :insumoId")
    Optional<StockInsumo> findByInsumoId(Long insumoId);

    @Query("SELECT s FROM StockInsumo s JOIN FETCH s.insumo WHERE s.insumo.activo = true")
    java.util.List<StockInsumo> findAllWithInsumoActivo();
}
