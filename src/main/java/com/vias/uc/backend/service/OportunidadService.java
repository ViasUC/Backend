package com.vias.uc.backend.service;

import com.vias.uc.backend.graphql.dto.CrearOportunidadInput;
import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Empresa;
import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.enums.EstadoOportunidad;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.EmpresaRepository;
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
    private final EmpresaRepository empresaRepository;

    public OportunidadService(OportunidadRepository oportunidadRepository,
                              UsuarioRepository usuarioRepository,
                              AuditoriaRepository auditoriaRepository,
                              EmpresaRepository empresaRepository) {
        this.oportunidadRepository = oportunidadRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.empresaRepository = empresaRepository;
    }

    public List<Oportunidad> porCreador(Long creadorId) {
        return oportunidadRepository.findAllByCreadorId(creadorId);
    }

    // ==== Crear ====

    public Oportunidad crearOportunidadEmpresa(CrearOportunidadInput input) {
        if (input == null) {
            throw new IllegalArgumentException("Input requerido");
        }

        System.out.println(">>> CREAR OPORTUNIDAD EMPRESA - Input recibido: " + input.getTitulo());
        System.out.println(">>> idCreador: " + input.getIdCreador());
        System.out.println(">>> fechaCierre: " + input.getFechaCierre());

        int creadorId = Integer.parseInt(input.getIdCreador().trim());

        Usuario creador = usuarioRepository.findById((long) creadorId)
                .orElseThrow(() -> new RuntimeException("Creador no encontrado: " + creadorId));

        assertRolHabilitado(creador);

        // Buscar la empresa del usuario
        Empresa empresa = empresaRepository.findByUsuarioId((long) creadorId)
                .orElse(null);
        
        if (empresa != null) {
            System.out.println(">>> Empresa encontrada para el usuario: " + empresa.getNombreEmpresa() + " (ID: " + empresa.getIdEmpresa() + ")");
        } else {
            System.out.println(">>> ADVERTENCIA: No se encontró empresa para el usuario " + creadorId);
        }

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
        
        // Asignar la empresa si existe
        if (empresa != null) {
            op.setEmpresa(empresa);
            System.out.println(">>> Empresa asignada a la oportunidad: " + empresa.getIdEmpresa());
        }

        // Nace siempre como BORRADOR, visible sólo para el creador
        op.setEstado(EstadoOportunidad.borrador);
        op.setFechaPublicacion(null);
        op.setIdAuditoria(audit.getIdAuditoria().intValue());

        System.out.println(">>> Guardando oportunidad en BD...");
        Oportunidad saved = oportunidadRepository.save(op);
        System.out.println(">>> Oportunidad guardada con ID: " + saved.getIdOportunidad());
        System.out.println(">>> ID Empresa en oportunidad guardada: " + (saved.getEmpresa() != null ? saved.getEmpresa().getIdEmpresa() : "NULL"));
        
        return saved;
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

        nuevoEstado = nuevoEstado.toLowerCase();
        
        // Convertir String a enum
        EstadoOportunidad estadoEnum;
        try {
            estadoEnum = EstadoOportunidad.valueOf(nuevoEstado);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }

        switch (estadoEnum) {
            case activo -> op.setFechaPublicacion(LocalDateTime.now());
            case borrador -> op.setFechaPublicacion(null);
            case pausada, cerrado -> { /* no tocamos fechaPublicacion */ }
        }

        op.setEstado(estadoEnum);
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
