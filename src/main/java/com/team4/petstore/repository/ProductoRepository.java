package com.team4.petstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.team4.petstore.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
