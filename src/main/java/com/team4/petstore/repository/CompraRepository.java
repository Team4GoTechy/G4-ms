package com.team4.petstore.repository;

import com.team4.petstore.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    @Query("SELECT c FROM Compra c JOIN FETCH c.detalles d JOIN FETCH d.producto WHERE c.usuario.id = :usuarioId ORDER BY c.fecha DESC")
    List<Compra> findByUsuarioIdWithDetalles(@Param("usuarioId") Long usuarioId);

    @Query("SELECT c FROM Compra c JOIN FETCH c.detalles d JOIN FETCH d.producto WHERE c.id = :id")
    @org.springframework.data.jpa.repository.Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Compra> findByIdWithDetalles(@Param("id") Long id);

    @Query("SELECT c FROM Compra c JOIN FETCH c.detalles d JOIN FETCH d.producto WHERE c.usuario.id = :usuarioId ORDER BY c.fecha DESC")
    List<Compra> findByUsuarioIdOrderByFechaDesc(@Param("usuarioId") Long usuarioId);

    @Query("SELECT c FROM Compra c JOIN FETCH c.detalles d JOIN FETCH d.producto ORDER BY c.fecha DESC")
    List<Compra> findAllWithDetalles();

    @Query("SELECT DISTINCT c FROM Compra c JOIN FETCH c.detalles d JOIN FETCH d.producto WHERE c.estado = com.team4.petstore.entity.EstadoCompra.PENDIENTE AND d.producto.id = :productoId")
    List<Compra> findPendingComprasByProductoId(@Param("productoId") Long productoId);
}
