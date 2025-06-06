package com.farmacia_gestion.controller.web;

import com.farmacia_gestion.model.User;
import com.farmacia_gestion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        User existing = userService.findByUsername(user.getUsername());
        if (existing != null) {
            model.addAttribute("registrationError", "Ya existe un usuario con ese nombre de usuario.");
            return "register";
        }
        userService.save(user);
        return "redirect:/login?registered";
    }

    @GetMapping({"/", "/index"}) // Mapeamos "/" y "/index" para servir la página principal
    public String showIndexPage() {
        return "index"; // Devuelve la vista index.html
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }
}