package com.vias.uc.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vias.uc.backend.model.Postulacion;
import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.model.Evidencia;
import com.vias.uc.backend.model.Portafolio;
import com.vias.uc.backend.model.dto.CrearPostulacionInput;
import com.vias.uc.backend.model.dto.EstadoPostulacionOutput;
import com.vias.uc.backend.model.dto.PostulacionOutput;
import com.vias.uc.backend.model.dto.VincularEvidenciasInput;
import com.vias.uc.backend.repository.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * F2 - Sistema de aplicación a postulaciones (Opción B para auditoría).
 * - Crear y enviar postulación
 * - Consultar estado
 * - Vincular evidencias del portafolio (IDs guardadas en JSON dentro de 'motivo')
 *
 * Nota clave de Opción B:
 *  - La tabla 'auditoria' genera un BIGINT (id_auditoria).
 *  - Aquí seguimos usando JDBC para INSERT ... RETURNING id_auditoria (Long),
 *    y lo convertimos con Math.toIntExact(...) para guardarlo en la columna INTEGER
 *    'postulaciones.id_auditoria' (sin relación JPA).
 */
@Service
public class PostulacionAplicacionService {

    private final PostulacionRepository postRepo;
    private final OportunidadRepository opRepo;
    private final EvidenciaRepository evRepo;
    private final PortafolioRepository portRepo;
    private final JdbcTemplate jdbc;
    private final ObjectMapper om = new ObjectMapper();
    private final AlumnoRepository alumnoRepo;

    public PostulacionAplicacionService(PostulacionRepository postRepo,
                                        OportunidadRepository opRepo,
                                        EvidenciaRepository evRepo,
                                        PortafolioRepository portRepo,
                                        AlumnoRepository alumnoRepo,
                                        JdbcTemplate jdbc) {
        this.postRepo = postRepo;
        this.opRepo = opRepo;
        this.evRepo = evRepo;
        this.portRepo = portRepo;
        this.alumnoRepo = alumnoRepo;
        this.jdbc = jdbc;
    }

    /**
     * Crear y enviar una postulación.
     * - Valida existencia de oportunidad
     * - Evita duplicado por (idPostulante, idOportunidad)
     * - Crea auditoría (INSERT ... RETURNING BIGINT) y guarda su id (INTEGER) en postulaciones.id_auditoria
     * - Estado inicial: 'pendiente'
     */
    @Transactional
    public PostulacionOutput crearYEnviar(CrearPostulacionInput in) {
        // 1) Validaciones
        if (in == null || in.idPostulante() == null || in.idOportunidad() == null) {
            throw new IllegalArgumentException("Parámetros incompletos");
        }

        Oportunidad op = opRepo.findById(in.idOportunidad())
                .orElseThrow(() -> new IllegalArgumentException("Oportunidad no existe"));

        if (op.getIdCreador() == null) {
            throw new IllegalArgumentException("La oportunidad no tiene ofertante asignado");
        }

        if (postRepo.existsByIdPostulanteAndIdOportunidad(in.idPostulante(), in.idOportunidad())) {
            throw new IllegalArgumentException("Ya postulaste a esta oportunidad");
        }

        // 2) Auditoría (BIGINT) y referencia en postulaciones (INTEGER)
        Long idAud = insertarAuditoria(
                in.idPostulante(),
                "POSTULACION_CREAR",
                "Creó y envió postulación a oportunidad " + in.idOportunidad()
        );

        // 3) Construcción y persistencia
        Postulacion p = new Postulacion();
        p.setIdOportunidad(in.idOportunidad());
        p.setIdPostulante(in.idPostulante());
        p.setIdOfertante(op.getIdCreador());
        p.setFechaPostulacion(LocalDateTime.now());
        p.setEstado("pendiente");
        p.setMotivo(buildMotivo(in.motivo(), List.of()));
        p.setIdAuditoria(Math.toIntExact(idAud)); // BIGINT -> INTEGER

        try {
            p = postRepo.save(p);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            String root = (e.getMostSpecificCause() != null)
                    ? e.getMostSpecificCause().getMessage()
                    : "Violación de integridad de datos";
            throw new IllegalArgumentException(root);
        }

        // 4) Respuesta
        return new PostulacionOutput(
                p.getIdPostulacion(),
                p.getEstado(),
                op.getTitulo(),
                p.getFechaPostulacion(),
                List.of()
        );
    }


    /**
     * Consulta el estado de una postulación para el alumno dueño.
     */
    @Transactional(readOnly = true)
    public EstadoPostulacionOutput estado(Integer idPostulacion, Integer idPostulante) {
        Postulacion p = postRepo.findByIdPostulacionAndIdPostulante(idPostulacion, idPostulante)
                .orElseThrow(() -> new IllegalArgumentException("Postulación no encontrada para este alumno"));

        Oportunidad op = opRepo.findById(p.getIdOportunidad()).orElse(null);
        List<Integer> evid = extractEvidencias(p.getMotivo());

        return new EstadoPostulacionOutput(
                p.getIdPostulacion(),
                p.getEstado(),
                p.getFechaPostulacion(),
                op != null ? op.getTitulo() : null,
                evid
        );
    }

    /**
     * Vincula evidencias (del portafolio del propio postulante) a una postulación ya creada.
     * Las IDs de evidencias se guardan en JSON dentro de la columna 'motivo' (sin modificar el esquema).
     */
    @Transactional
    public PostulacionOutput vincularEvidencias(VincularEvidenciasInput in, Integer idPostulante) {
        // 0) Validación de entrada
        if (in == null || in.idPostulacion() == null || in.idsEvidencias() == null || in.idsEvidencias().isEmpty()) {
            throw new IllegalArgumentException("Debes enviar al menos una evidencia");
        }
        if (in.idsEvidencias().stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Hay IDs de evidencias nulos");
        }

        // 1) Cargar postulación y verificar dueño
        Postulacion p = postRepo.findById(in.idPostulacion())
                .orElseThrow(() -> new IllegalArgumentException("Postulación no existe"));

        if (!p.getIdPostulante().equals(idPostulante)) {
            throw new SecurityException("No podés modificar una postulación de otro alumno");
        }

        // 1.1) Estado editable
        if (!"pendiente".equalsIgnoreCase(p.getEstado())) {
            throw new IllegalStateException("No podés modificar evidencias en una postulación no pendiente");
        }


        // 3) Validar pertenencia de todas las evidencias al portafolio del postulante
        Portafolio port = portRepo.findByIdUsuario(idPostulante)
                .orElseThrow(() -> new IllegalStateException("El usuario no tiene portafolio"));


        long countValidas = evRepo.countByIdEvidenciaInAndIdPortafolio(in.idsEvidencias(), port.getIdPortafolio());
        if (countValidas != in.idsEvidencias().size()) {
            throw new IllegalArgumentException("Alguna evidencia no existe o no pertenece a tu portafolio");
        }

        // 4) Merge sin duplicados + orden estable (idempotente)
        List<Integer> actuales = extractEvidencias(p.getMotivo()); // tolera motivo vacío/JSON inválido
        LinkedHashSet<Integer> merged = new LinkedHashSet<>(actuales);
        int antes = merged.size();
        in.idsEvidencias().forEach(merged::add);
        int agregadas = merged.size() - antes;

        // Si no hubo cambios, devolvemos tal cual (idempotente)
        if (agregadas == 0) {
            Oportunidad op = opRepo.findById(p.getIdOportunidad()).orElse(null);
            return new PostulacionOutput(
                    p.getIdPostulacion(),
                    p.getEstado(),
                    op != null ? op.getTitulo() : null,
                    p.getFechaPostulacion(),
                    new ArrayList<>(merged)
            );
        }

        // 5) Persistir motivo JSON actualizado (texto libre se conserva)
        String textoLibre = extractMotivoLibre(p.getMotivo());
        p.setMotivo(buildMotivo(textoLibre, new ArrayList<>(merged)));

        try {
            p = postRepo.save(p);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            String root = (e.getMostSpecificCause() != null) ? e.getMostSpecificCause().getMessage() : "Integridad de datos";
            throw new IllegalArgumentException(root);
        }

        // 6) Auditoría del vínculo
        insertarAuditoria(
                idPostulante,
                "POSTULACION_VINCULAR_EVIDENCIAS",
                "Postulación " + p.getIdPostulacion() + " agregó " + agregadas + " evidencias (total: " + merged.size() + ")"
        );

        Oportunidad op = opRepo.findById(p.getIdOportunidad()).orElse(null);

        return new PostulacionOutput(
                p.getIdPostulacion(),
                p.getEstado(),
                op != null ? op.getTitulo() : null,
                p.getFechaPostulacion(),
                new ArrayList<>(merged)
        );
    }


    // ===================== Helpers =====================

    /**
     * Inserta una fila en auditoría y retorna su id_auditoria (bigint).
     * fecha_evento usa DEFAULT NOW() en la BD.
     */
    private Long insertarAuditoria(Integer actorId, String accion, String detalle) {
        return jdbc.queryForObject(
                "INSERT INTO auditoria(actor_id, accion, detalle) VALUES (?, ?, ?) RETURNING id_auditoria",
                Long.class, actorId, accion, detalle
        );
    }

    /** Construye el JSON para la columna 'motivo': {"motivo": "...", "evidencias":[...]} */
    private String buildMotivo(String motivoLibre, List<Integer> evidencias) {
        ObjectNode root = om.createObjectNode();
        if (motivoLibre != null && !motivoLibre.isBlank()) {
            root.put("motivo", motivoLibre);
        }
        ArrayNode arr = om.createArrayNode();
        evidencias.forEach(arr::add);
        root.set("evidencias", arr);
        return root.toString();
    }

    /** Extrae la lista de IDs de evidencias desde 'motivo' (si existe). */
    private List<Integer> extractEvidencias(String motivo) {
        if (motivo == null || motivo.isBlank()) return List.of();
        try {
            JsonNode node = om.readTree(motivo);
            if (node.has("evidencias") && node.get("evidencias").isArray()) {
                List<Integer> out = new ArrayList<>();
                node.get("evidencias").forEach(n -> out.add(n.asInt()));
                return out;
            }
        } catch (Exception ignored) { }
        return List.of();
    }

    /** Extrae el texto libre "motivo" del JSON almacenado en 'motivo'. */
    private String extractMotivoLibre(String motivo) {
        if (motivo == null || motivo.isBlank()) return null;
        try {
            JsonNode node = om.readTree(motivo);
            return node.has("motivo") ? node.get("motivo").asText(null) : null;
        } catch (Exception ignored) {
            return null;
        }
    }
}
