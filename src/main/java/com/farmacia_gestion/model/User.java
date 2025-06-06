package com.farmacia_gestion.model;

import jakarta.persistence.*; // Asegúrate de usar jakarta.persistence si estás en Spring Boot 3+
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users") // Asegúrate de que el nombre de la tabla sea 'users' si lo usas en el H2 Console
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true, nullable = false) // Asegúrate de que el username sea único y no nulo
    private String username;

    @Column(nullable = false) // La contraseña no puede ser nula
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // Carga los roles eagerly y en cascada
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>(); // Inicializa la colección para evitar NullPointerExceptions

    // --- Constructor VACÍO (Requerido por JPA) ---
    public User() {
        // Constructor vacío requerido por JPA
    }

    // --- Constructor que DataLoader está intentando usar ---
    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        // Los roles se pueden establecer por separado o con otro constructor si es necesario
    }

    // --- Getters y Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // Método de conveniencia para añadir un solo rol
    public void addRole(Role role) {
        this.roles.add(role);
    }
}