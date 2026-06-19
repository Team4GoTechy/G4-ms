package com.team4.petstore.repository;

import com.team4.petstore.entity.SolicitudReposicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface SolicitudReposicionRepository extends JpaRepository<SolicitudReposicion, Long> {
    @Query("SELECT s FROM SolicitudReposicion s JOIN FETCH s.veterinario WHERE s.id = :id")
    Optional<SolicitudReposicion> findByIdWithVeterinario(@Param("id") Long id);

    @Query("SELECT s FROM SolicitudReposicion s JOIN FETCH s.detalles d JOIN FETCH d.insumo WHERE s.id = :id")
    Optional<SolicitudReposicion> findByIdWithDetalles(@Param("id") Long id);

    @Query("SELECT s FROM SolicitudReposicion s JOIN FETCH s.detalles d JOIN FETCH d.insumo WHERE s.veterinario.id = :veterinarioId ORDER BY s.fechaCreacion DESC")
    List<SolicitudReposicion> findByVeterinarioIdWithDetalles(@Param("veterinarioId") Long veterinarioId);

    @Query("SELECT s FROM SolicitudReposicion s JOIN FETCH s.detalles d JOIN FETCH d.insumo ORDER BY s.fechaCreacion DESC")
    List<SolicitudReposicion> findAllWithDetalles();

    List<SolicitudReposicion> findByVeterinarioIdOrderByFechaCreacionDesc(Long veterinarioId);
}
