package com.team4.petstore.repository;

import com.team4.petstore.entity.EvolucionInternacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvolucionInternacionRepository extends JpaRepository<EvolucionInternacion, Long> {

    List<EvolucionInternacion> findByInternacionIdOrderByFechaHoraAsc(Long internacionId);
}