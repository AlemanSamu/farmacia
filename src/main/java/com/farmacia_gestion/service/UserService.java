package com.farmacia_gestion.service;

import com.farmacia_gestion.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(User user);
    User findByUsername(String username);
}