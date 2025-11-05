package com.vias.uc.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;   // <---
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilita la protección CSRF (Cross-Site Request Forgery)
                .csrf(AbstractHttpConfigurer::disable)
                // Configura las reglas de autorización de las solicitudes
                .authorizeHttpRequests(auth -> auth
                        // Permite que CUALQUIER solicitud (anyRequest) sea accedida sin autenticación
                        .anyRequest().permitAll()
                );
        return http.build();
    }

    @Bean                                           // <---
    public BCryptPasswordEncoder passwordEncoder() { // <---
        return new BCryptPasswordEncoder();         // <---
    }                                               // <---
}
