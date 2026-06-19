package com.team4.petstore.repository;
import com.team4.petstore.entity.DetallePrescripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetallePrescripcionRepository extends JpaRepository<DetallePrescripcion, Long> {

    List<DetallePrescripcion> findByPrescripcionId(Long prescripcionId);
}