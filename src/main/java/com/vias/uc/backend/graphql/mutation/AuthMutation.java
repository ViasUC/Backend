package com.vias.uc.backend.graphql.mutation;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.dto.LoginInput;
import com.vias.uc.backend.service.AuthService;
import com.vias.uc.backend.service.RegistroService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Controller
public class AuthMutation {

    private final AuthService authService;
    private final RegistroService registroService;

    @Value("${app.jwt.secret:defaultSuperSecretKeyForHS256_ChangeThisNow_12345678901234567890}")
    private String jwtSecret;

    public AuthMutation(AuthService authService, RegistroService registroService) {
        this.authService = authService;
        this.registroService = registroService;
    }

    @MutationMapping
    public LoginResponse login(@Argument("input") LoginInput input) {
        System.out.println(">>> LOGIN recibido: " + input.email());
        Usuario usuario = authService.login(input.email(), input.password());
        
        if (usuario == null) {
            throw new RuntimeException("Credenciales inválidas");
        }
        
        // Generar token JWT
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        String token = Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("idUsuario", usuario.getIdUsuario())
                .claim("rol", usuario.getRolPrincipal() != null ? usuario.getRolPrincipal().toString() : "alumno")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(60 * 60 * 4))) // 4 horas
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        
        return new LoginResponse(
            token,
            usuario.getIdUsuario().toString(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getRolPrincipal() != null ? usuario.getRolPrincipal().toString() : "alumno"
        );
    }

    @MutationMapping
    public RegisterResponse register(@Argument("input") RegistroService.RegisterInput input) {
        System.out.println(">>> REGISTER recibido: " + input.getEmail() + " - Tipo: " + input.getTipoUsuario());
        
        // Registrar usuario (y empresa si aplica)
        Usuario usuario = registroService.registrarUsuario(input);
        
        // Generar token JWT
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        String token = Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("idUsuario", usuario.getIdUsuario())
                .claim("rol", usuario.getRolPrincipal() != null ? usuario.getRolPrincipal().toString() : "ALUMNO")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(60 * 60 * 4))) // 4 horas
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        
        // Crear respuesta
        UserRegistered userRegistered = new UserRegistered(
            usuario.getIdUsuario(),
            usuario.getNombre(),
            usuario.getApellido(),
            usuario.getEmail(),
            usuario.getRolPrincipal() != null ? usuario.getRolPrincipal().toString() : "ALUMNO"
        );
        
        return new RegisterResponse(token, userRegistered, true, "Usuario registrado exitosamente");
    }

    // DTOs para la respuesta de registro
    public record RegisterResponse(String token, UserRegistered usuario, boolean success, String message) {}
    public record UserRegistered(Long idUsuario, String nombre, String apellido, String email, String rol) {}
    
    // DTO para la respuesta de login
    public record LoginResponse(String token, String idUsuario, String nombre, String apellido, String rolPrincipal) {}
}
