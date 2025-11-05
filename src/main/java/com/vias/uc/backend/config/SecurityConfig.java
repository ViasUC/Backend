package com.vias.uc.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // desactiva CSRF (solo en desarrollo)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/**").permitAll() // permite todos los endpoints de /api
                .anyRequest().permitAll() // permite cualquier otro
            )
            .formLogin(login -> login.disable()) // desactiva el formulario de login HTML
            .httpBasic(basic -> basic.disable()); // desactiva autenticación básica
        return http.build();
    }
}
