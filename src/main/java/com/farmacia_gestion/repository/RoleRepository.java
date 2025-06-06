package com.farmacia_gestion.repository;

import com.farmacia_gestion.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // ¡Importa Optional!

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); // <-- ¡Cambia aquí para que devuelva Optional!
}