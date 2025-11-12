package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario login(String email, String password) {

        System.out.println(">>> LOGIN llamado con email = " + email);

        // TEST DEFINITIVO
        System.out.println(">>> PROBANDO QUERY findAll()");
        usuarioRepository.findAll().forEach(u ->
            System.out.println("USER EN BD: " + u.getEmail())
        );

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email incorrecto"));

        boolean passwordOk;

        if (usuario.getPassword().startsWith("$2a$")) {
            passwordOk = passwordEncoder.matches(password, usuario.getPassword());
        } else {
            passwordOk = usuario.getPassword().equals(password);
        }

        if (!passwordOk) {
            throw new RuntimeException("Password incorrecto");
        }

        return usuario;
    }
}
