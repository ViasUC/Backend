package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.dto.RegistroAlumnoInput;
import com.vias.uc.backend.repository.AlumnoRepository;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import com.vias.uc.backend.model.dto.RegistroAlumnoInput;
import com.vias.uc.backend.model.dto.UsuarioRegistroInput;
import com.vias.uc.backend.model.enums.RolUsuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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



// ...


    @Transactional
    public Alumno registrarAlumno(RegistroAlumnoInput input) {
        if (input == null || input.usuario() == null) {
            throw new IllegalArgumentException("El objeto 'usuario' es obligatorio.");
        }
        UsuarioRegistroInput ui = input.usuario();

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
        audit.setDetalle(
                (input.detalleAuditoria() != null && !input.detalleAuditoria().isBlank())
                        ? input.detalleAuditoria() : "registro alumno"
        );
        audit = auditoriaRepository.save(audit);

        // Usuario
        Usuario u = new Usuario();
        u.setNombre(ui.nombre());
        u.setApellido(ui.apellido());
        u.setUbicacion(ui.ubicacion());
        u.setTelefono(ui.telefono());
        u.setEmail(ui.email());
        u.setPassword(passwordEncoder.encode(ui.password()));
        u.setCompletitud(ui.completitud() != null ? ui.completitud() : 0);
        // mapear enum -> String (BD sigue siendo VARCHAR/ENUM PG)
        RolUsuario rol = ui.rolPrincipal() != null ? ui.rolPrincipal() : RolUsuario.alumno;
        u.setRolPrincipal(rol);
        u.setIdAuditoria(audit.getIdAuditoria());
        u = usuarioRepository.save(u);

        // Alumno (PK compartida con Usuario)
        Alumno a = new Alumno();
        a.setUsuario(u);                  // @MapsId
        a.setCarrera(input.carrera());
        a.setSemestre(input.semestre());
        a.setIdAuditoria(audit.getIdAuditoria());

        return alumnoRepository.save(a);
    }


    @Transactional(readOnly = true)
    public Alumno getAlumno(Integer id) {
        return alumnoRepository.findByIdUsuario(id)
                .orElse(null);
    }
}
