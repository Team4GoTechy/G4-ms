package com.team4.petstore.repository;
import com.team4.petstore.entity.Prescripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrescripcionRepository extends JpaRepository<Prescripcion, Long> {

    Optional<Prescripcion> findByConsultaId(Long consultaId);
}