package com.farmacia_gestion.service;

import com.farmacia_gestion.model.Venta;
import com.farmacia_gestion.model.DetalleVenta;
import com.farmacia_gestion.model.Producto;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.Map;

public interface VentaService {
    List<Venta> findAll();
    Optional<Venta> findById(Long id);
    Venta save(Venta venta);
    void deleteById(Long id);
    List<Venta> findSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<Venta> findSalesByClientName(String clientName);
    Venta createSale(Long clienteId, Map<Long, Integer> productosConCantidades);
}