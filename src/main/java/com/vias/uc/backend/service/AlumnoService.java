package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.AlumnoRepository;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AlumnoService {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final AuditoriaRepository auditoriaRepository;

    public AlumnoService(UsuarioRepository usuarioRepository,
                         AlumnoRepository alumnoRepository,
                         AuditoriaRepository auditoriaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.alumnoRepository = alumnoRepository;
        this.auditoriaRepository = auditoriaRepository;
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
        return alumnoRepository.findById(id);
    }

    /* =========================
       Escrituras
       ========================= */

    /**
     * Crea un alumno y, si no existe, también su Usuario asociado.
     * El rol se guarda como String plano: "alumno".
     */
    @Transactional
    public Alumno crearAlumno(String nombre,
                              String apellido,
                              String email,
                              String carrera,
                              Integer semestre) {

        // Auditoría para la operación de crear alumno (se usará en Alumno)
        Auditoria auAlumno = Auditoria.builder()
                .accion("CREAR_ALUMNO")
                .detalle("Creación de alumno para " + email)
                .actorId(null) // setear si tenés usuario autenticado
                .fechaEvento(LocalDateTime.now())
                .build();
        auditoriaRepository.save(auAlumno);

        // Buscar o crear Usuario
        Usuario usuario = usuarioRepository.findByEmail(email).orElseGet(() -> {
            // Auditoría específica para el Usuario nuevo
            Auditoria auUsuario = Auditoria.builder()
                    .accion("CREAR_USUARIO")
                    .detalle("Alta de usuario desde creación de alumno: " + email)
                    .actorId(null)
                    .fechaEvento(LocalDateTime.now())
                    .build();
            auditoriaRepository.save(auUsuario);

            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setApellido(apellido);
            u.setEmail(email);
            u.setRolPrincipal("alumno");
            u.setCompletitud(0);
            // Relación obligatoria con auditoría en usuarios (id_auditoria NOT NULL)
            u.setAuditoria(auUsuario);

            return usuarioRepository.save(u);
        });

        // Crear Alumno asociado
        Alumno alumno = new Alumno();
        alumno.setUsuario(usuario);
        alumno.setCarrera(carrera);
        alumno.setSemestre(semestre);

        // 🔴 FIX CLAVE: setear la auditoría del Alumno (columna NOT NULL)
        alumno.setAuditoria(auAlumno);

        return alumnoRepository.save(alumno);
    }
}
