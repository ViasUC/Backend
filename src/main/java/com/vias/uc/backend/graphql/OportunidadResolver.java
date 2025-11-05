package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.OportunidadRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

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

        // 1) Crear registro de auditoría (evita NOT NULL en oportunidades.id_auditoria)
        Auditoria audit = Auditoria.builder()
                .actorId(safeParseInt(input.idCreador()))
                .accion("CREAR_OPORTUNIDAD")
                .detalle(buildDetalleAuditoria(input))
                // fechaEvento se setea en @PrePersist si viene null
                .build();
        audit = auditoriaRepository.save(audit);

        // 2) Crear oportunidad
        Oportunidad op = new Oportunidad();
        op.setIdCreador(safeParseInt(input.idCreador()));
        op.setTitulo(trimOrNull(input.titulo()));
        op.setDescripcion(trimOrNull(input.descripcion()));
        op.setRequisitos(trimOrNull(input.requisitos()));
        op.setUbicacion(trimOrNull(input.ubicacion()));
        op.setModalidad(trimOrNull(input.modalidad()));
        op.setTipo(trimOrNull(input.tipo()));
        op.setFechaPublicacion(LocalDateTime.now());
        op.setFechaCierre(parseFechaCierre(input.fechaCierre()));

        String estado = (input.estado() == null || input.estado().isBlank())
                ? "activo"
                : input.estado().trim();
        op.setEstado(estado);

        // Importante: setear id_auditoria (BD lo requiere NOT NULL)
        // Auditoria.id es Long (bigserial) y oportunidades.id_auditoria es int4 -> cast seguro si cabe
        op.setIdAuditoria(audit.getIdAuditoria().intValue());

        return oportunidadRepository.save(op);
    }

    // =========================
    // Field Resolver: Oportunidad.creador
    // =========================
    @SchemaMapping(typeName = "Oportunidad", field = "creador")
    public Usuario resolverCreador(Oportunidad oportunidad) {
        return usuarioRepository.findById(Long.valueOf(oportunidad.getIdCreador()))
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
            String estado
    ) {}
}
