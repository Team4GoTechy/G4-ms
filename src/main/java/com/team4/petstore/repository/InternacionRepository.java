package com.team4.petstore.repository;
import com.team4.petstore.entity.Internacion;
import com.team4.petstore.entity.enums.EstadoInternacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InternacionRepository extends JpaRepository<Internacion, Long> {

    List<Internacion> findByEstado(EstadoInternacion estado);

    List<Internacion> findByMascotaId(Long mascotaId);

    List<Internacion> findByMascotaIdAndEstado(Long mascotaId, EstadoInternacion estado);
}