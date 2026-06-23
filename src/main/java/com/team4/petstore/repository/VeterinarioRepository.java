package com.team4.petstore.repository;
import com.team4.petstore.entity.Veterinario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VeterinarioRepository extends JpaRepository<Veterinario, Long> {

    Optional<Veterinario> findByUsuarioId(Long usuarioId);

    Optional<Veterinario> findByMatricula(String matricula);

    boolean existsByMatricula(String matricula);

    List<Veterinario> findByActivoTrue();
}