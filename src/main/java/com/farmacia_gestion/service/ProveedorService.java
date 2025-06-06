package com.farmacia_gestion.service;

import com.farmacia_gestion.model.Proveedor;
import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    List<Proveedor> findAll();
    Optional<Proveedor> findById(Long id);
    Proveedor save(Proveedor proveedor);
    void deleteById(Long id);
    List<Proveedor> searchByNombre(String nombre);
}