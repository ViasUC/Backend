package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.dto.RegistroAlumnoInput;
import com.vias.uc.backend.repository.AlumnoRepository;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.model.dto.UsuarioRegistroInput;
import com.vias.uc.backend.model.enums.RolUsuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import com.vias.uc.backend.model.dto.UsuarioInput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AlumnoService {

    private static final Logger log = LoggerFactory.getLogger(AlumnoService.class);

    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AlumnoService(
            AlumnoRepository alumnoRepository,
            UsuarioRepository usuarioRepository,
            AuditoriaRepository auditoriaRepository
    ) {
        this.alumnoRepository = alumnoRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaRepository = auditoriaRepository;
    }


    // ========================================================================
    // REGISTRO
    // ========================================================================

    @Transactional
    public Alumno registrarAlumnoMutationFede(RegistroAlumnoInput input) {
        log.info("📥 [REGISTRO ALUMNO] Input recibido: {}", input);

        if (input == null || input.usuario() == null) {
            throw new IllegalArgumentException("El objeto 'usuario' es obligatorio.");
        }

        UsuarioRegistroInput ui = input.usuario();
        log.debug("👉 UsuarioRegistroInput: {}", ui);

        if (ui.email() == null || ui.email().isBlank()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (ui.password() == null || ui.password().isBlank()) {
            throw new IllegalArgumentException("El password es obligatorio.");
        }

        if (usuarioRepository.existsByEmail(ui.email())) {
            throw new IllegalArgumentException("El email ya está registrado: " + ui.email());
        }

        // Auditoría
        Auditoria audit = new Auditoria();
        audit.setAccion("create");
        audit.setDetalle(input.detalleAuditoria());
        audit = auditoriaRepository.save(audit);

        log.debug("📝 Auditoría creada con ID {}", audit.getIdAuditoria());

        // Usuario
        Usuario u = new Usuario();
        u.setNombre(ui.nombre());
        u.setApellido(ui.apellido());
        u.setUbicacion(ui.ubicacion());
        u.setTelefono(ui.telefono());
        u.setEmail(ui.email());
        u.setPassword(passwordEncoder.encode(ui.password()));
        u.setCompletitud(ui.completitud() != null ? ui.completitud() : 0);

        RolUsuario rol = ui.rolPrincipal() != null ? ui.rolPrincipal() : RolUsuario.alumno;
        u.setRolPrincipal(rol);
        u.setIdAuditoria(audit.getIdAuditoria());
        u = usuarioRepository.save(u);

        log.info("✅ Usuario creado con ID {}", u.getIdUsuario());

        // Alumno
        Alumno a = new Alumno();
        a.setUsuario(u);
        a.setCarrera(input.carrera());
        a.setSemestre(input.semestre());
        a.setIdAuditoria(audit.getIdAuditoria().longValue());

        Alumno saved = alumnoRepository.save(a);
        log.info("🎓 Alumno registrado con éxito: {}", saved);

        return saved;
    }


    // ========================================================================
    // OBTENER
    // ========================================================================

    @Transactional(readOnly = true)
    public Alumno getAlumno(Integer id) {
        log.info("🔍 Buscando alumno con ID {}", id);
        return alumnoRepository.findByIdUsuario(id)
                .orElse(null);
    }


    // ========================================================================
    // ACTUALIZAR
    // ========================================================================

    @Transactional
    public Alumno actualizarAlumno(Integer id, com.vias.uc.backend.model.dto.AlumnoInput input) {

        log.info("📥 [ACTUALIZAR ALUMNO] ID={}, Input={}", id, input);

        try {
            // buscar alumno
            Alumno alumno = alumnoRepository.findByIdUsuario(id)
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id_usuario: " + id));

            Usuario usuario = alumno.getUsuario();
            if (usuario == null) {
                throw new RuntimeException("El alumno no tiene un usuario asociado (id_usuario=" + id + ")");
            }

            log.debug("🔎 Alumno encontrado: {}", alumno);
            log.debug("🔎 Usuario actual: {}", usuario);

            // actualizar usuario
            if (input.usuario() != null) {
                var ui = input.usuario();

                log.info("📝 Actualizando datos del usuario...");

                log.debug("Nombre nuevo: {}", ui.nombre());
                log.debug("Apellido nuevo: {}", ui.apellido());
                log.debug("Email nuevo: {}", ui.email());
                log.debug("Teléfono nuevo: {}", ui.telefono());
                log.debug("Ubicación nueva: {}", ui.ubicacion());

                if (ui.nombre() != null) usuario.setNombre(ui.nombre());
                if (ui.apellido() != null) usuario.setApellido(ui.apellido());
                if (ui.email() != null && !ui.email().isBlank()) usuario.setEmail(ui.email());
                if (ui.telefono() != null) usuario.setTelefono(ui.telefono());
                if (ui.ubicacion() != null) usuario.setUbicacion(ui.ubicacion());
            }

            // actualizar alumno
            if (input.carrera() != null) {
                log.info("📝 Actualizando carrera: {}", input.carrera());
                alumno.setCarrera(input.carrera());
            }

            if (input.semestre() != null) {
                log.info("📝 Actualizando semestre: {}", input.semestre());
                alumno.setSemestre(input.semestre());
            }

            usuarioRepository.save(usuario);
            Alumno actualizado = alumnoRepository.save(alumno);

            log.info("✅ Alumno actualizado correctamente: {}", actualizado);
            return actualizado;

        } catch (Exception e) {
            log.error("❌ ERROR ACTUALIZANDO ALUMNO ID={}: {}", id, e.getMessage(), e);
            throw e; // dejar que GraphQL lo capture
        }
    }
}
