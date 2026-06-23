package com.team4.petstore.repository;

import com.team4.petstore.entity.BloqueoFecha;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface BloqueoFechaRepository extends JpaRepository<BloqueoFecha, Long> {

    List<BloqueoFecha> findByVeterinarioIdOrderByFechaInicioAsc(Long veterinarioId);

    List<BloqueoFecha> findByVeterinarioIdAndFechaInicioBetween(Long veterinarioId, LocalDate fechaInicio, LocalDate fechaFin);

    boolean existsByVeterinarioIdAndFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(
            Long veterinarioId, LocalDate fechaInicio, LocalDate fechaFin);
}
