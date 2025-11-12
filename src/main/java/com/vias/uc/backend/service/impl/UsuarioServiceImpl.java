package com.vias.uc.backend.service.impl;

import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Investigador;
import com.vias.uc.backend.model.Profesor;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.enums.RolUsuario;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.InvestigadorRepository;
import com.vias.uc.backend.repository.ProfesorRepository;
import com.vias.uc.backend.repository.UsuarioLiteRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioLiteRepository repo;

    // >>> Agregados <<<
    private final UsuarioRepository usuarioRepository;
    private final ProfesorRepository profesorRepository;
    private final InvestigadorRepository investigadorRepository;
    private final AuditoriaRepository auditoriaRepository;
    private final PasswordEncoder passwordEncoder; // BCrypt

    @Override
    public boolean exists(Integer id) {
        return repo.existsByIdSimple(id);
    }

    @Override
    public String rol(Integer id) {
        return repo.rol(id);
    }

    @Override
    public boolean activo(Integer id) {
        Boolean v = repo.activo(id);
        return v == null ? true : v;
    }

    // ===== Altas =====

    @Override
    @Transactional
    public Profesor registrarProfesor(UsuarioService.ProfesorInput input) {
        var ui = input.getUsuario();
        String email = safeEmail(ui);
        if (isBlank(email))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email requerido");
        if (usuarioExisteEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya registrado");

        Auditoria audit = auditoriaRepository.save(nuevaAuditoria("REGISTRO_PROFESOR", email));

        Usuario u = new Usuario();
        u.setNombre(ns(ui.getNombre()));
        u.setApellido(ns(ui.getApellido()));
        u.setEmail(email);
        u.setTelefono(ns(ui.getTelefono()));
        u.setUbicacion(ns(ui.getUbicacion()));
        u.setPassword(passwordEncoder.encode(ns(ui.getPassword()))); // BCRYPT
        u.setRolPrincipal(RolUsuario.profesor);
        u.setCompletitud(0);
        u.setAuditoria(audit);
        usuarioRepository.save(u);

        Profesor p = new Profesor();
        p.setIdUsuario(u.getIdUsuario());              // PK = FK a usuarios
        p.setDepartamento(input.getDepartamento());
        p.setCategoriaDocente(input.getCategoriaDocente());
        p.setAreasDocentes(input.getAreasDocentes());
        p.setIdAuditoria(audit.getIdAuditoria());
        profesorRepository.save(p);

        // para que GraphQL no reciba null en Profesor.usuario (Usuario!)
        p.setUsuario(u);

        return p;
    }

    @Override
    @Transactional
    public Investigador registrarInvestigador(UsuarioService.InvestigadorInput input) {
        var ui = input.getUsuario();
        String email = safeEmail(ui);
        if (isBlank(email))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email requerido");
        if (usuarioExisteEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya registrado");

        Auditoria audit = auditoriaRepository.save(nuevaAuditoria("REGISTRO_INVESTIGADOR", email));

        Usuario u = new Usuario();
        u.setNombre(ns(ui.getNombre()));
        u.setApellido(ns(ui.getApellido()));
        u.setEmail(email);
        u.setTelefono(ns(ui.getTelefono()));
        u.setUbicacion(ns(ui.getUbicacion()));
        u.setPassword(passwordEncoder.encode(ns(ui.getPassword()))); // BCRYPT
        u.setRolPrincipal(RolUsuario.investigador);
        u.setCompletitud(0);
        u.setAuditoria(audit);
        usuarioRepository.save(u);

        Investigador i = new Investigador();
        i.setIdUsuario(u.getIdUsuario());              // PK = FK a usuarios
        i.setAreasInvestigacion(input.getAreasInvestigacion());
        i.setAfiliaciones(input.getAfiliaciones());
        i.setHindex(input.getHindex());
        i.setIdAuditoria(audit.getIdAuditoria());
        investigadorRepository.save(i);

        // completa el subobjeto requerido por el schema
        i.setUsuario(u);

        return i;
    }

    // ===== Helpers =====

    private Auditoria nuevaAuditoria(String accion, String detalle) {
        Auditoria a = new Auditoria();
        a.setAccion(accion);
        a.setDetalle(detalle);
        a.setFechaEvento(LocalDateTime.now());
        // si tu tabla exige actorId NOT NULL ajustá acá
        a.setActorId(0);
        return a;
    }

    private boolean usuarioExisteEmail(String email) {
        try { return usuarioRepository.existsByEmailIgnoreCase(email); }
        catch (Exception ignore) { return usuarioRepository.findByEmailIgnoreCase(email).isPresent(); }
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private static String ns(String s) { return s == null ? "" : s; }

    private String safeEmail(UsuarioService.UsuarioInput in) {
        if (in == null || in.getEmail() == null) return null;
        return in.getEmail().trim().toLowerCase();
    }
}
