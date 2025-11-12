package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.dto.AlumnoInput;
import com.vias.uc.backend.model.dto.RegistroAlumnoInput;
import com.vias.uc.backend.repository.AlumnoRepository;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.UsuarioRepository;

import org.springframework.stereotype.Service;


import com.vias.uc.backend.model.dto.UsuarioRegistroInput;
import com.vias.uc.backend.model.enums.RolUsuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import com.vias.uc.backend.model.dto.UsuarioInput;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// imports necesarios: List, Optional, LocalDateTime, Transactional, etc.
@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AlumnoService(AlumnoRepository alumnoRepository,
                         UsuarioRepository usuarioRepository,
                         AuditoriaRepository auditoriaRepository) {
        this.alumnoRepository = alumnoRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional
    public Alumno registrarAlumno(RegistroAlumnoInput input) {
        if (input == null || input.usuario() == null) {
            throw new IllegalArgumentException("usuario es obligatorio");
        }
        var ui = input.usuario();
        if (ui.email() == null || ui.email().isBlank()) {
            throw new IllegalArgumentException("email obligatorio");
        }
        if (ui.password() == null || ui.password().isBlank()) {
            throw new IllegalArgumentException("password obligatorio");
        }

        // Si el correo YA existe: solo crear Alumno (si no existe) y vincular al Usuario
        if (usuarioRepository.existsByEmail(ui.email())) {
            Usuario u = usuarioRepository.findByEmail(ui.email())
                    .orElseThrow(() -> new IllegalStateException("usuario no encontrado tras existsByEmail"));
            if (alumnoRepository.existsById(u.getIdUsuario().longValue())) {
                throw new IllegalStateException("El email ya tiene Alumno");
            }

            Auditoria auAlumno = new Auditoria();
            auAlumno.setAccion("CREAR_ALUMNO");
            auAlumno.setDetalle("Creación de alumno para " + ui.email());
            auAlumno.setFechaEvento(LocalDateTime.now());
            auAlumno = auditoriaRepository.save(auAlumno);

            Alumno a = new Alumno();
            a.setUsuario(u);
            a.setCarrera(input.carrera());
            a.setSemestre(input.semestre());
            a.setAuditoria(auAlumno);
            return alumnoRepository.save(a);
        }

        // Si el correo NO existe: crear Usuario (con password del input) + Alumno
        Auditoria auUsuario = new Auditoria();
        auUsuario.setAccion("CREAR_USUARIO");
        auUsuario.setDetalle("Alta de usuario (alumno) " + ui.email());
        auUsuario.setFechaEvento(LocalDateTime.now());
        auUsuario = auditoriaRepository.save(auUsuario);

        Usuario u = new Usuario();
        u.setNombre(ui.nombre());
        u.setApellido(ui.apellido());
        u.setEmail(ui.email());
        u.setTelefono(ui.telefono());
        u.setUbicacion(ui.ubicacion());
        u.setRolPrincipal(RolUsuario.alumno);
        u.setCompletitud(ui.completitud() != null ? ui.completitud() : 0);
        u.setPassword(passwordEncoder.encode(ui.password()));
        u.setAuditoria(auUsuario);
        u = usuarioRepository.save(u);

        Auditoria auAlumno = new Auditoria();
        auAlumno.setAccion("CREAR_ALUMNO");
        auAlumno.setDetalle("Creación de alumno para " + ui.email());
        auAlumno.setFechaEvento(LocalDateTime.now());
        auAlumno = auditoriaRepository.save(auAlumno);

        Alumno a = new Alumno();
        a.setUsuario(u);
        a.setCarrera(input.carrera());
        a.setSemestre(input.semestre());
        a.setAuditoria(auAlumno);
        return alumnoRepository.save(a);
    }

    @Transactional
    public Alumno actualizarAlumno(Long id, AlumnoInput input) {
        Alumno alumno = alumnoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado id=" + id));
        Usuario usuario = alumno.getUsuario();

        if (input.usuario() != null) {
            var ui = input.usuario();
            if (ui.nombre() != null) usuario.setNombre(ui.nombre());
            if (ui.apellido() != null) usuario.setApellido(ui.apellido());
            if (ui.email() != null) usuario.setEmail(ui.email());
            if (ui.telefono() != null) usuario.setTelefono(ui.telefono());
            if (ui.ubicacion() != null) usuario.setUbicacion(ui.ubicacion());
            usuarioRepository.save(usuario);
        }
        if (input.carrera() != null) alumno.setCarrera(input.carrera());
        if (input.semestre() != null) alumno.setSemestre(input.semestre());
        return alumnoRepository.save(alumno);
    }

    @Transactional(readOnly = true)
    public List<Alumno> listarAlumnos() {
        return alumnoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Alumno> obtenerAlumnoPorId(Long id) {
        return alumnoRepository.findById(id);
    }
}
