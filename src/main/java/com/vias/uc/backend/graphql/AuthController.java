package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final ProfesorRepository profesorRepository;
    private final InvestigadorRepository investigadorRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${app.jwt.secret:defaultSuperSecretKeyForHS256_ChangeThisNow_12345678901234567890}")
    private String jwtSecret;

    public AuthController(UsuarioRepository usuarioRepository,
                          ProfesorRepository profesorRepository,
                          InvestigadorRepository investigadorRepository) {
        this.usuarioRepository = usuarioRepository;
        this.profesorRepository = profesorRepository;
        this.investigadorRepository = investigadorRepository;
    }

    @MutationMapping
    public AuthPayload loginDocenteInvestigador(@Argument LoginInput input) {
        System.out.println(">> Login docente/investigador: " + input.email());

        Usuario usuario = usuarioRepository.findByEmail(input.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email no registrado"));

        // 🔐 Compara la contraseña ingresada con el hash guardado
        if (!passwordEncoder.matches(input.password(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Contraseña incorrecta");
        }

        boolean esProfesor = profesorRepository.existsByIdUsuario(Math.toIntExact(usuario.getIdUsuario()));
        boolean esInvestigador = investigadorRepository.existsByIdUsuario(Math.toIntExact(usuario.getIdUsuario()));

        if (!esProfesor && !esInvestigador) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo profesores o investigadores pueden iniciar sesión");
        }

        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        String token = Jwts.builder()
                .setSubject(usuario.getEmail())
                .claim("idUsuario", usuario.getIdUsuario())
                .claim("rol", usuario.getRolPrincipal() != null ? usuario.getRolPrincipal().toString() : "DOCENTE")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(60 * 60 * 4))) // 4 horas
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new AuthPayload(token, usuario);
    }

    public record LoginInput(String email, String password) {}
    public record AuthPayload(String token, Usuario usuario) {}
}
