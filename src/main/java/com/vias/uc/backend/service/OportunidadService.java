package com.vias.uc.backend.service;

import com.vias.uc.backend.graphql.dto.CrearOportunidadInput;
import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.OportunidadRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class OportunidadService {

    private final OportunidadRepository oportunidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaRepository auditoriaRepository;

    public OportunidadService(OportunidadRepository oportunidadRepository,
                              UsuarioRepository usuarioRepository,
                              AuditoriaRepository auditoriaRepository) {
        this.oportunidadRepository = oportunidadRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    public List<Oportunidad> porCreador(Long creadorId) {
        return oportunidadRepository.findAllByCreadorId(creadorId);
    }

    // ==== Crear ====

    public Oportunidad crearOportunidadEmpresa(CrearOportunidadInput input) {
        if (input == null) {
            throw new IllegalArgumentException("Input requerido");
        }

        int creadorId = Integer.parseInt(input.getIdCreador().trim());

        Usuario creador = usuarioRepository.findById((long) creadorId)
                .orElseThrow(() -> new RuntimeException("Creador no encontrado: " + creadorId));

        assertRolHabilitado(creador);

        Auditoria audit = Auditoria.builder()
                .actorId(creadorId)
                .accion("CREAR_OPORTUNIDAD")
                .detalle("Creación de oportunidad: " + input.getTitulo())
                .build();
        audit = auditoriaRepository.save(audit);

        Oportunidad op = new Oportunidad();
        op.setIdCreador(creadorId);
        op.setTitulo(trimOrNull(input.getTitulo()));
        op.setDescripcion(trimOrNull(input.getDescripcion()));
        op.setRequisitos(trimOrNull(input.getRequisitos()));
        op.setUbicacion(trimOrNull(input.getUbicacion()));
        op.setModalidad(trimOrNull(input.getModalidad()));
        op.setTipo(trimOrNull(input.getTipo()));
        op.setFechaCierre(parseFecha(input.getFechaCierre()));

        // Nace siempre como BORRADOR, visible sólo para el creador
        op.setEstado("BORRADOR");
        op.setFechaPublicacion(null);
        op.setIdAuditoria(audit.getIdAuditoria().intValue());

        return oportunidadRepository.save(op);
    }

    // ==== Actualizar ====

    public Oportunidad actualizarOportunidad(Integer id,
                                             CrearOportunidadInput input,
                                             Long idActor) {
        Oportunidad op = oportunidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada"));

        assertPuedeEditar(idActor, op);

        op.setTitulo(trimOrNull(input.getTitulo()));
        op.setDescripcion(trimOrNull(input.getDescripcion()));
        op.setRequisitos(trimOrNull(input.getRequisitos()));
        op.setUbicacion(trimOrNull(input.getUbicacion()));
        op.setModalidad(trimOrNull(input.getModalidad()));
        op.setTipo(trimOrNull(input.getTipo()));
        op.setFechaCierre(parseFecha(input.getFechaCierre()));

        return oportunidadRepository.save(op);
    }

    // ==== Cambiar estado ====

    public Oportunidad cambiarEstado(Integer id, String nuevoEstado, Long idActor) {
        Oportunidad op = oportunidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada"));

        assertPuedeEditar(idActor, op);

        nuevoEstado = nuevoEstado.toUpperCase();

        switch (nuevoEstado) {
            case "ACTIVA" -> op.setFechaPublicacion(LocalDateTime.now());
            case "BORRADOR" -> op.setFechaPublicacion(null);
            case "PAUSADA", "CERRADA" -> { /* no tocamos fechaPublicacion */ }
            default -> throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }

        op.setEstado(nuevoEstado);
        return oportunidadRepository.save(op);
    }

    // ==== Eliminar ====

    public boolean eliminar(Integer id, Long idActor) {
        Oportunidad op = oportunidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada"));

        assertPuedeEditar(idActor, op);
        oportunidadRepository.delete(op);
        return true;
    }

    // ==== Helpers ====

    private LocalDateTime parseFecha(String s) {
        if (s == null || s.isBlank()) return null;
        return LocalDateTime.parse(s);
    }

    private String trimOrNull(String s) {
        return (s == null) ? null : s.trim();
    }

    private void assertPuedeEditar(Long idActor, Oportunidad op) {
        if (idActor == null) {
            throw new AccessDeniedException("Usuario actor requerido");
        }

        Long idCreador = op.getIdCreador() != null ? op.getIdCreador().longValue() : null;

        boolean esCreador = idCreador != null && idActor.equals(idCreador);

        Usuario actor = usuarioRepository.findById(idActor)
                .orElseThrow(() -> new RuntimeException("Usuario actor no encontrado"));

        boolean esAdmin = actor.getRolPrincipal() != null &&
                actor.getRolPrincipal().name().equalsIgnoreCase("administrador");

        if (!(esCreador || esAdmin)) {
            throw new AccessDeniedException("No autorizado para modificar esta oportunidad");
        }
    }

    private static final Set<String> ROLES_HABILITADOS = Set.of(
            "administrador", "profesor", "investigador", "empresa", "empleador"
    );

    private void assertRolHabilitado(Usuario usuario) {
        if (usuario == null || usuario.getRolPrincipal() == null) {
            throw new AccessDeniedException("Rol no definido");
        }
        String rol = usuario.getRolPrincipal().name().toLowerCase();
        if (!ROLES_HABILITADOS.contains(rol)) {
            throw new AccessDeniedException(
                    "Rol no autorizado para crear oportunidades"
            );
        }
    }
}
