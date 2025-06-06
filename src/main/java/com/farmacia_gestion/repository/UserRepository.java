package com.farmacia_gestion.repository;

import com.farmacia_gestion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // ¡Importa Optional!

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // <-- ¡Cambia aquí para que devuelva Optional!
}