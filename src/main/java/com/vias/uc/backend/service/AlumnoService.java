package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.enums.RolUsuario;
import com.vias.uc.backend.repository.AlumnoRepository;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AlumnoService(UsuarioRepository usuarioRepository,
                         AlumnoRepository alumnoRepository,
                         AuditoriaRepository auditoriaRepository,
                         BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.alumnoRepository = alumnoRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* =========================
       Lecturas
       ========================= */
    @Transactional(readOnly = true)
    public List<Alumno> listarAlumnos() {
        return alumnoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Alumno> obtenerAlumnoPorId(Long id) {
        return alumnoRepository.findById(Math.toIntExact(id));
    }

    /* =========================
       Escrituras
       ========================= */
    /**
     * Crea un alumno y, si no existe, también su Usuario asociado.
     * - Rol: RolUsuario.alumno
     * - Password: se genera temporal y se cifra SOLO si el usuario es nuevo
     */
    @Transactional
    public Alumno crearAlumno(String nombre,
                              String apellido,
                              String email,
                              String carrera,
                              Integer semestre) {

        // Auditoría para la operación de crear alumno (se usará en Alumno)
        Auditoria auAlumno = auditoriaRepository.save(
                Auditoria.builder()
                        .accion("CREAR_ALUMNO")
                        .detalle("Creación de alumno para " + email)
                        .actorId(null) // setear si hay usuario autenticado
                        .fechaEvento(LocalDateTime.now())
                        .build()
        );

        // Buscar o crear Usuario (managed)
        Usuario usuario = usuarioRepository.findByEmail(email).orElseGet(() -> {
            Auditoria auUsuario = auditoriaRepository.save(
                    Auditoria.builder()
                            .accion("CREAR_USUARIO")
                            .detalle("Alta de usuario desde creación de alumno: " + email)
                            .actorId(null)
                            .fechaEvento(LocalDateTime.now())
                            .build()
            );

            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setApellido(apellido);
            u.setEmail(email);
            u.setRolPrincipal(RolUsuario.alumno);
            u.setCompletitud(0);
            u.setAuditoria(auUsuario);

            String temporal = generarPasswordTemporal(12);
            u.setPassword(passwordEncoder.encode(temporal));

            return usuarioRepository.save(u);
        });

        // Si el usuario existía sin password/rol, completar
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            String temporal = generarPasswordTemporal(12);
            usuario.setPassword(passwordEncoder.encode(temporal));
            if (usuario.getRolPrincipal() == null) {
                usuario.setRolPrincipal(RolUsuario.alumno);
            }
            usuarioRepository.save(usuario);
        }

        // Evitar duplicado por PK compartida (id_alumno = id_usuario)
        Integer idUsuario = usuario.getIdUsuario();
        if (idUsuario != null && alumnoRepository.existsById(idUsuario)) {
            throw new IllegalStateException(
                    "El usuario " + email + " ya posee un Alumno con id=" + idUsuario
            );
        }

        // Crear Alumno asociado (idAlumno debe quedar NULL aquí; @MapsId la copiará)
        Alumno alumno = new Alumno();
        alumno.setUsuario(usuario);
        alumno.setCarrera(carrera);
        alumno.setSemestre(semestre);
        alumno.setAuditoria(auAlumno);

        return alumnoRepository.save(alumno);
    }

    /* =========================
       Helpers
       ========================= */
    private static final String ABC = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
    private static final SecureRandom RNG = new SecureRandom();

    private String generarPasswordTemporal(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(ABC.charAt(RNG.nextInt(ABC.length())));
        return sb.toString();
    }
}
