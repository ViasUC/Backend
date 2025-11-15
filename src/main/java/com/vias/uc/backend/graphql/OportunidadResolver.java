package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.enums.EstadoOportunidad;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.OportunidadRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.AccessDeniedException;
import java.util.Set;


import java.time.LocalDateTime;
import java.util.List;

@Controller
public class OportunidadResolver {

    private final OportunidadRepository oportunidadRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaRepository auditoriaRepository;

    public OportunidadResolver(OportunidadRepository oportunidadRepository,
                               UsuarioRepository usuarioRepository,
                               AuditoriaRepository auditoriaRepository) {
        this.oportunidadRepository = oportunidadRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    // =========================
    // Query
    // =========================
    @QueryMapping
    public List<Oportunidad> oportunidades() {
        return oportunidadRepository.findAll();
    }

    @QueryMapping
    public Oportunidad oportunidad(@Argument Integer id) {
        return oportunidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + id));
    }

    // =========================
    // Mutation: crear oportunidad (docente)
    // =========================

    @MutationMapping
    public Oportunidad crearOportunidadDocente(@Argument CrearOportunidadInput input) {
        validarInput(input);

        // 0) Resolver y validar el creador por rol (chequeo manual)
        int creadorId = safeParseInt(input.idCreador());
        Usuario creador = usuarioRepository.findById((long) creadorId)
                .orElseThrow(() -> new RuntimeException("Creador no encontrado: " + creadorId));
        assertRolHabilitado(creador);

        // 1) Crear registro de auditoría
        Auditoria audit = Auditoria.builder()
                .actorId(creadorId)
                .accion("CREAR_OPORTUNIDAD")
                .detalle(buildDetalleAuditoria(input))
                .build();
        audit = auditoriaRepository.save(audit);

        // 2) Crear oportunidad (forzamos el id del creador validado)
        Oportunidad op = new Oportunidad();
        op.setIdCreador(creadorId);
        op.setTitulo(trimOrNull(input.titulo()));
        op.setDescripcion(trimOrNull(input.descripcion()));
        op.setRequisitos(trimOrNull(input.requisitos()));
        op.setUbicacion(trimOrNull(input.ubicacion()));
        op.setModalidad(trimOrNull(input.modalidad()));
        op.setTipo(trimOrNull(input.tipo()));
        op.setFechaPublicacion(LocalDateTime.now());
        op.setFechaCierre(parseFechaCierre(input.fechaCierre()));

        EstadoOportunidad estado = (input.estado() == null)
                ? EstadoOportunidad.activo
                : input.estado();

        op.setEstado(estado);


        op.setIdAuditoria(audit.getIdAuditoria().intValue());

        return oportunidadRepository.save(op);
    }

    @MutationMapping
    public Oportunidad editarOportunidad(@Argument EditarOportunidadInput input) {
        if (input == null) throw new IllegalArgumentException("Input requerido");
        if (input.idOportunidad() == null) throw new IllegalArgumentException("idOportunidad es obligatorio");
        if (isBlank(input.idEditor())) throw new IllegalArgumentException("idEditor es obligatorio");

        int editorId = safeParseInt(input.idEditor());

        // 1) Resolver y validar el usuario editor por rol
        Usuario editor = usuarioRepository.findById((long) editorId)
                .orElseThrow(() -> new RuntimeException("Editor no encontrado: " + editorId));
        assertRolHabilitado(editor);

        // 2) Buscar la oportunidad
        Oportunidad op = oportunidadRepository.findById(input.idOportunidad())
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + input.idOportunidad()));

        // 2.b) Validar que solo el creador o un administrador puedan editar
        String rolEditor = editor.getRolPrincipal().name().toLowerCase();
        boolean esCreador = editor.getIdUsuario().equals(op.getIdCreador());
        boolean esAdmin = "administrador".equals(rolEditor);

        if (!esCreador && !esAdmin) {
            throw new AccessDeniedException("Solo el creador o un administrador pueden editar la oportunidad");
        }



        // 3) Actualizar solo los campos que vienen con valor
        if (!isBlank(input.titulo())) op.setTitulo(trimOrNull(input.titulo()));
        if (!isBlank(input.descripcion())) op.setDescripcion(trimOrNull(input.descripcion()));
        if (!isBlank(input.requisitos())) op.setRequisitos(trimOrNull(input.requisitos()));
        if (!isBlank(input.ubicacion())) op.setUbicacion(trimOrNull(input.ubicacion()));
        if (!isBlank(input.modalidad())) op.setModalidad(trimOrNull(input.modalidad()));
        if (!isBlank(input.tipo())) op.setTipo(trimOrNull(input.tipo()));

        if (!isBlank(input.fechaCierre())) {
            var cierre = parseFechaCierre(input.fechaCierre());
            if (cierre != null && cierre.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("fechaCierre no puede ser en el pasado");
            }
            op.setFechaCierre(cierre);
        }

        if (input.estado() != null) {
            op.setEstado(input.estado());
        }

        // 4) Registrar auditoría de edición
        Auditoria audit = Auditoria.builder()
                .actorId(editorId)
                .accion("EDITAR_OPORTUNIDAD")
                .detalle("Edición de oportunidad id=" + op.getIdOportunidad())
                .build();
        audit = auditoriaRepository.save(audit);

        op.setIdAuditoria(audit.getIdAuditoria().intValue());

        // 5) Guardar cambios
        return oportunidadRepository.save(op);
    }














    // =========================
    // Field Resolver: Oportunidad.creador
    // =========================
    @SchemaMapping(typeName = "Oportunidad", field = "creador")
    public Usuario resolverCreador(Oportunidad oportunidad) {
        return usuarioRepository.findById((long) Math.toIntExact(Long.valueOf(oportunidad.getIdCreador())))
                .orElseThrow(() -> new RuntimeException("Creador no encontrado: " + oportunidad.getIdCreador()));
    }

    // =========================
    // Helpers
    // =========================
    private void validarInput(CrearOportunidadInput input) {
        if (input == null) throw new IllegalArgumentException("Input requerido");
        if (isBlank(input.idCreador())) throw new IllegalArgumentException("idCreador es obligatorio");
        if (isBlank(input.titulo())) throw new IllegalArgumentException("titulo es obligatorio");
        if (isBlank(input.modalidad())) throw new IllegalArgumentException("modalidad es obligatoria");
        if (isBlank(input.tipo())) throw new IllegalArgumentException("tipo es obligatorio");
        LocalDateTime cierre = parseFechaCierre(input.fechaCierre());
        if (cierre != null && cierre.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("fechaCierre no puede ser en el pasado");
        }
    }

    private String buildDetalleAuditoria(CrearOportunidadInput input) {
        String t = trimOrNull(input.titulo());
        String tp = trimOrNull(input.tipo());
        return (t != null || tp != null)
                ? String.format("Creación de oportunidad: %s%s", t != null ? t : "",
                tp != null ? " (" + tp + ")" : "")
                : "Creación de oportunidad";
    }

    private LocalDateTime parseFechaCierre(String fecha) {
        if (isBlank(fecha)) return null;
        // Espera formato ISO-8601: 2025-12-31T23:59:00
        return LocalDateTime.parse(fecha);
    }

    private int safeParseInt(String s) {
        return Integer.parseInt(s.trim());
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private String trimOrNull(String s) { return s == null ? null : s.trim(); }

    // Input local como record (para no crear otro archivo)
    public static record CrearOportunidadInput(
            String idCreador,
            String titulo,
            String descripcion,
            String requisitos,
            String ubicacion,
            String modalidad,
            String tipo,
            String fechaCierre,
            EstadoOportunidad estado
    ) {}

    public static record EditarOportunidadInput(
            Integer idOportunidad,
            String idEditor,
            String titulo,
            String descripcion,
            String requisitos,
            String ubicacion,
            String modalidad,
            String tipo,
            String fechaCierre,
            EstadoOportunidad estado
    ) {}



    @QueryMapping
    public List<Oportunidad> oportunidadesPorCreador(@Argument Long creadorId) {
        return oportunidadRepository.findAllByCreadorId(creadorId);
    }

    private static final Set<String> ROLES_HABILITADOS = Set.of(
            "administrador", "profesor", "investigador", "empresa"
    );

    private void assertRolHabilitado(Usuario usuario) {
        if (usuario == null) throw new AccessDeniedException("Usuario no encontrado");
        if (usuario.getRolPrincipal() == null) throw new AccessDeniedException("Rol no definido");

        // Enum -> String usando name(), no trim()
        String rol = usuario.getRolPrincipal().name().toLowerCase();

        if (!ROLES_HABILITADOS.contains(rol)) {
            throw new AccessDeniedException(
                    "No autorizado: solo administrador/profesor/investigador/empresa pueden publicar oportunidades"
            );
        }
    }


}

