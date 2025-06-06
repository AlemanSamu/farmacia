package com.farmacia_gestion.service;

import com.farmacia_gestion.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> findAll();
    Optional<Producto> findById(Long id);
    Producto save(Producto producto);
    void deleteById(Long id);
    List<Producto> searchByNombre(String nombre);
    List<Producto> findLowStockProducts();
}