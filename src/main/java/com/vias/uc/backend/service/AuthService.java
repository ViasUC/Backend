package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoriaService;
    private final SesionService sesionService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository, AuditoriaService auditoriaService, SesionService sesionService) {
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
        this.sesionService = sesionService;
    }

    public Usuario login(String email, String password) {

        System.out.println(">>> LOGIN llamado con email = " + email);

        // TEST DEFINITIVO
        System.out.println(">>> PROBANDO QUERY findAll()");
        usuarioRepository.findAll().forEach(u ->
            System.out.println("USER EN BD: " + u.getEmail())
        );

        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        
        if (usuario == null) {
            System.out.println(">>> Usuario no encontrado: " + email);
            
            // Registrar intento de login con usuario no encontrado
            auditoriaService.crear("LOGIN_FALLIDO", "Intento de login con email no registrado: " + email, null);
            
            return null;
        }

        boolean passwordOk;

        // Verificar si el password está hasheado con bcrypt ($2a$, $2b$, $2y$)
        if (usuario.getPassword().startsWith("$2a$") || 
            usuario.getPassword().startsWith("$2b$") || 
            usuario.getPassword().startsWith("$2y$")) {
            System.out.println(">>> Verificando password hasheado con bcrypt");
            passwordOk = passwordEncoder.matches(password, usuario.getPassword());
        } else {
            System.out.println(">>> Verificando password en texto plano");
            passwordOk = usuario.getPassword().equals(password);
        }

        if (!passwordOk) {
            System.out.println(">>> Password incorrecto para: " + email);
            System.out.println(">>> Hash en BD: " + usuario.getPassword().substring(0, 20) + "...");
            
            // Registrar intento de login fallido
            auditoriaService.crear("LOGIN_FALLIDO", "Contraseña incorrecta para: " + email, usuario.getIdUsuario().intValue());
            
            return null;
        }

        System.out.println(">>> Login exitoso para: " + email);
        
        // Registrar login exitoso en sesión (esto también crea el registro en auditoría)
        sesionService.registrarLoginExitoso(usuario.getIdUsuario().intValue(), email);
        
        return usuario;
    }
}
