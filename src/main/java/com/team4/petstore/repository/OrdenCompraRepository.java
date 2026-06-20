package com.team4.petstore.repository;

import com.team4.petstore.entity.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    @Query("SELECT o FROM OrdenCompra o JOIN FETCH o.proveedor WHERE o.id = :id")
    Optional<OrdenCompra> findByIdWithProveedor(@Param("id") Long id);

    @Query("SELECT o FROM OrdenCompra o JOIN FETCH o.detalles d JOIN FETCH d.insumo WHERE o.id = :id")
    Optional<OrdenCompra> findByIdWithDetalles(@Param("id") Long id);

    @Query("SELECT o FROM OrdenCompra o JOIN FETCH o.detalles d JOIN FETCH d.insumo JOIN FETCH o.proveedor ORDER BY o.fechaCreacion DESC")
    List<OrdenCompra> findAllWithDetallesAndProveedor();
}
