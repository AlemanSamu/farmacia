package com.farmacia_gestion.repository;

import com.farmacia_gestion.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByFechaVentaBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Venta> findByClienteNombreContainingIgnoreCase(String clienteNombre);
}