package com.team4.petstore.repository;
import com.team4.petstore.entity.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    List<Servicio> findByVeterinarioId(Long veterinarioId);
}