package com.team4.petstore.repository;

import com.team4.petstore.entity.HorarioAtencion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, Long> {

    List<HorarioAtencion> findByVeterinarioIdOrderByDiaSemanaAsc(Long veterinarioId);

    Optional<HorarioAtencion> findByVeterinarioIdAndDiaSemana(Long veterinarioId, Integer diaSemana);

    void deleteByVeterinarioId(Long veterinarioId);
}
