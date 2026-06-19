package com.team4.petstore.repository;
import com.team4.petstore.entity.ArchivoClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArchivoClinicoRepository extends JpaRepository<ArchivoClinico, Long> {

    List<ArchivoClinico> findByConsultaId(Long consultaId);
}