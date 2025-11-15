package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Portafolio;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.dto.AlumnoInput;
import com.vias.uc.backend.model.dto.RegistroAlumnoInput;
import com.vias.uc.backend.model.dto.AlumnoPerfilOutput;
import com.vias.uc.backend.repository.AlumnoRepository;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.PortafolioRepository;
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
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

// imports necesarios: List, Optional, LocalDateTime, Transactional, etc.
@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final PortafolioRepository portafolioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AlumnoService(AlumnoRepository alumnoRepository,
                         UsuarioRepository usuarioRepository,
                         AuditoriaRepository auditoriaRepository,
                         PortafolioRepository portafolioRepository) {
        this.alumnoRepository = alumnoRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.portafolioRepository = portafolioRepository;
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

        Auditoria auPortafolio = new Auditoria();
        auPortafolio.setAccion("CREAR_PORTAFOLIO");
        auPortafolio.setDetalle("Portafolio 1:1 para usuario " + u.getEmail());
        auPortafolio.setFechaEvento(LocalDateTime.now());
        auPortafolio = auditoriaRepository.save(auPortafolio);

        // Crear Portafolio 1–a-1 inmutable (PK compartida)
        Portafolio p = new Portafolio();
        p.setDescripcion(null);
        p.setSkills(null);
        p.setVisibilidad(true);                   // o false, como definas
        p.setUltimaActualizacion(LocalDateTime.now());
        p.setIdAuditoria(Long.valueOf(auPortafolio.getIdAuditoria())); // <<<<<< esto evita el NULL
        portafolioRepository.save(p);
        // Crear Alumno
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

        Alumno actualizado = alumnoRepository.save(alumno);
        mostrarConfirmacion("OK");

        // Llamada “trivial” para cumplir con el diagrama
        return actualizado;
    }

    // (→ “mostrarConfirmacion(estado)” en el diagrama)
    private Alumno mostrarConfirmacion(String estado) { //diagrama de secuencia
        // Podés devolver el mismo alumno, null o incluso imprimir algo
        System.out.println("mostrarConfirmacion(" + estado + ")");
        return null; // o retornar algo como alumno actualizado, si preferís
    }


    @Transactional(readOnly = true)
    public List<Alumno> listarAlumnos() {
        return alumnoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Alumno> obtenerAlumnoPorId(Long id) {
        return alumnoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public AlumnoPerfilOutput consultarPerfil(Long idUsuario) { // (→ "consultarPerfil" en el diagrama)
        Alumno alumno = alumnoRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado id=" + idUsuario));

        Usuario u = alumno.getUsuario();

        // Luego el backend “envía” los datos a la interfaz
        return mostrarDatosPerfil(u, alumno); //diagrama de secuencia
    }

    // (→ "mostrarDatosPerfil(datos)" en el diagrama)
    private AlumnoPerfilOutput mostrarDatosPerfil(Usuario u, Alumno alumno) {
        return new AlumnoPerfilOutput(
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getTelefono(),
                u.getUbicacion(),
                alumno.getCarrera(),
                alumno.getSemestre()
        );
    }


}
