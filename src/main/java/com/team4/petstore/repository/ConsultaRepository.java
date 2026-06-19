package com.team4.petstore.repository;
import com.team4.petstore.entity.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    @Query("SELECT c FROM Consulta c WHERE c.mascota.id = :mascotaId ORDER BY c.fechaCreacion DESC")
    Page<Consulta> findHistorialByMascotaId(@Param("mascotaId") Long mascotaId, Pageable pageable);

    Page<Consulta> findByVeterinarioId(Long veterinarioId, Pageable pageable);
}