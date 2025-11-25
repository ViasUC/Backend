package com.vias.uc.backend.service;

import com.vias.uc.backend.model.EmpresaUsuario;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.enums.RolUsuario;
import com.vias.uc.backend.repository.EmpresaUsuarioRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaUsuarioRepository empresaUsuarioRepository;
    private final AuditoriaService auditoriaService;
    private final SesionService sesionService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(
        UsuarioRepository usuarioRepository,
        EmpresaUsuarioRepository empresaUsuarioRepository,
        AuditoriaService auditoriaService, 
        SesionService sesionService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.empresaUsuarioRepository = empresaUsuarioRepository;
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

        // Si es EMPLEADOR, verificar que tenga al menos una relación activa con alguna empresa
        if (usuario.getRolPrincipal() == RolUsuario.empresa) {
            System.out.println(">>> Verificando estado activo para empleador: " + email);
            
            List<EmpresaUsuario> relacionesActivas = empresaUsuarioRepository.findByUsuarioAndActivoTrue(usuario.getIdUsuario());
            
            if (relacionesActivas.isEmpty()) {
                System.out.println(">>> Usuario empleador sin relaciones activas (pendiente de aprobación)");
                
                // Registrar intento de login de usuario pendiente
                auditoriaService.crear(
                    "LOGIN_PENDIENTE_APROBACION", 
                    "Intento de login de usuario pendiente de aprobación: " + email, 
                    usuario.getIdUsuario().intValue()
                );
                
                // Lanzar excepción específica para usuarios pendientes
                throw new RuntimeException("PENDIENTE_APROBACION:Tu acceso está pendiente de aprobación por el administrador de la empresa.");
            }
            
            System.out.println(">>> Usuario empleador con " + relacionesActivas.size() + " relación(es) activa(s)");
        }

        System.out.println(">>> Login exitoso para: " + email);
        
        // Registrar login exitoso en sesión (esto también crea el registro en auditoría)
        sesionService.registrarLoginExitoso(usuario.getIdUsuario().intValue(), email);
        
        return usuario;
    }

    /**
     * Verifica si un email está disponible para registro
     * @param email Email a verificar
     * @return true si está disponible, false si ya existe
     */
    public boolean isEmailDisponible(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return !usuarioRepository.existsByEmail(email.trim().toLowerCase());
    }
}
