package com.team4.petstore.repository;
import com.team4.petstore.entity.Cita;
import com.team4.petstore.entity.enums.EstadoCita;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByVeterinarioIdAndFechaHoraBetween(
        Long veterinarioId, LocalDateTime inicio, LocalDateTime fin);

    Page<Cita> findByMascotaId(Long mascotaId, Pageable pageable);

    List<Cita> findByEstadoAndFechaHoraBetween(
        EstadoCita estado, LocalDateTime inicio, LocalDateTime fin);
    
    List<Cita> findByVeterinarioId(Long veterinarioId);

    // Valida solapamiento de horario para el mismo veterinario.
    // excluirCitaId se usa al reagendar (para no chocar contra sí misma); en creación se pasa null.
    
    @Query(value = """
        SELECT COUNT(*) > 0 FROM citas c
        WHERE c.veterinario_id = :veterinarioId
          AND c.estado NOT IN ('CANCELADA', 'NO_ASISTIO')
          AND c.id <> COALESCE(:excluirCitaId, -1)
          AND c.fecha_hora < :finPropuesto
          AND (c.fecha_hora + (c.duracion_min || ' minutes')::interval) > :inicioPropuesto
        """, nativeQuery = true)
    boolean existeSolapamiento(
        @Param("veterinarioId") Long veterinarioId,
        @Param("inicioPropuesto") LocalDateTime inicioPropuesto,
        @Param("finPropuesto") LocalDateTime finPropuesto,
        @Param("excluirCitaId") Long excluirCitaId);
}