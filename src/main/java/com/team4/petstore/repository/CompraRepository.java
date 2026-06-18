package com.team4.petstore.repository;

import com.team4.petstore.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    @Query("SELECT c FROM Compra c JOIN FETCH c.detalles d JOIN FETCH d.producto WHERE c.usuario.id = :usuarioId ORDER BY c.fecha DESC")
    List<Compra> findByUsuarioIdWithDetalles(@Param("usuarioId") Long usuarioId);
}
