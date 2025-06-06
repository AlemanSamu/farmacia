package com.farmacia_gestion.service;

import com.farmacia_gestion.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<Cliente> findAll();
    Optional<Cliente> findById(Long id);
    Cliente save(Cliente cliente);
    void deleteById(Long id);
    List<Cliente> searchByNombre(String nombre);
    Cliente findByDni(String dni);
}