package com.farmacia_gestion.config;

import com.farmacia_gestion.service.UserService;
// import org.springframework.beans.factory.annotation.Autowired; // Puedes eliminar esta importación si solo usas el constructor de beans
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    // El constructor con @Autowired y userService comentado o eliminado si no se usa directamente en la clase
    // private final UserService userService;
    // public SecurityConfig(UserService userService) {
    //      this.userService = userService;
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // DESHABILITADO PARA DESARROLLO - ¡Cuidado en PRODUCCIÓN!
            .authorizeHttpRequests(authorize -> authorize
                // Se eliminan o comentan las reglas de seguridad específicas si quieres que todo sea público
                // .requestMatchers("/", "/index", "/login", "/h2-console/**", "/css/**", "/js/**", "/images/**", "/register").permitAll()
                // .requestMatchers("/admin/**", "/usuarios/**").hasRole("ADMINISTRADOR")
                // .requestMatchers("/productos/**", "/proveedores/**", "/clientes/**", "/ventas/**").hasAnyRole("ADMINISTRADOR", "EMPLEADO")
                // .anyRequest().authenticated()

                // ESTA LÍNEA ES LA CLAVE PARA PERMITIR EL ACCESO A CUALQUIER URL
                .anyRequest().permitAll()
            )
            // Se mantienen las configuraciones de formLogin y logout, aunque no tendrán efecto
            // inmediato con .anyRequest().permitAll()
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/authenticateTheUser")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .permitAll()
            )
            .headers(headers -> headers.frameOptions().disable());

        return http.build();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index");
        registry.addViewController("/index").setViewName("index");
    }
}