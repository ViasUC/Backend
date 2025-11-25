package com.vias.uc.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditoriaService {
    private final AuditoriaRepository repo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Auditoria crear(String accion, String detalle, Integer actorId) {
        return repo.save(Auditoria.builder()
                .accion(accion)
                .detalle(detalle)
                .actorId(actorId)
                .build());
    }

    public Integer log(int userId, String accion, String detalle) {
        Auditoria a = crear(accion, detalle, userId);
        return Math.toIntExact(a.getIdAuditoria());
    }

    /**
     * Registrar una invitacion a candidato.
     * Guarda en el campo detalle un JSON con toda la info de la invitacion.
     */
    public Auditoria registrarInvitacion(
            Integer idUsuario,
            Integer idEmpresa,
            Integer idOportunidad,
            Integer idConvenio,
            String tipoInvitacion, // "OPORTUNIDAD" o "CONVENIO"
            String canal, // "EMAIL" o "WHATSAPP"
            String mensaje,
            Integer actorId
    ) {
        try {
            // Truncar el mensaje si es muy largo para evitar exceder el limite de 255 caracteres del JSON
            String mensajeResumido = mensaje;
            if (mensaje != null && mensaje.length() > 100) {
                mensajeResumido = mensaje.substring(0, 97) + "...";
            }
            
            Map<String, Object> detalleMap = new HashMap<>();
            detalleMap.put("idUsuario", idUsuario);
            detalleMap.put("idEmpresa", idEmpresa);
            if (idOportunidad != null) detalleMap.put("idOportunidad", idOportunidad);
            if (idConvenio != null) detalleMap.put("idConvenio", idConvenio);
            detalleMap.put("tipo", tipoInvitacion);
            detalleMap.put("canal", canal);
            detalleMap.put("mensaje", mensajeResumido);

            String detalleJson = objectMapper.writeValueAsString(detalleMap);
            
            // Verificar que el JSON no exceda 255 caracteres
            if (detalleJson.length() > 255) {
                // Si aun es muy largo, acortar mas el mensaje
                mensajeResumido = mensaje != null && mensaje.length() > 50 
                    ? mensaje.substring(0, 47) + "..." 
                    : mensaje;
                detalleMap.put("mensaje", mensajeResumido);
                detalleJson = objectMapper.writeValueAsString(detalleMap);
            }

            return crear("INVITACION_" + tipoInvitacion, detalleJson, actorId);
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar invitacion en auditoria", e);
        }
    }

    /**
     * Verificar si ya existe una invitacion previa.
     * Busca en auditoria registros con accion INVITACION_* y parsea el JSON del detalle.
     */
    public Optional<Auditoria> verificarInvitacionExistente(
            Integer idUsuario,
            Integer idOportunidad,
            Integer idConvenio
    ) {
        try {
            String accionBuscada = idOportunidad != null ? "INVITACION_OPORTUNIDAD" : "INVITACION_CONVENIO";
            
            // Buscar todos los registros de invitaciones
            List<Auditoria> invitaciones = repo.findAll().stream()
                    .filter(a -> a.getAccion() != null && a.getAccion().equals(accionBuscada))
                    .toList();

            for (Auditoria auditoria : invitaciones) {
                if (auditoria.getDetalle() == null) continue;

                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> detalleMap = objectMapper.readValue(
                            auditoria.getDetalle(),
                            Map.class
                    );

                    Integer auditoriaIdUsuario = (Integer) detalleMap.get("idUsuario");
                    
                    if (idOportunidad != null) {
                        Integer auditoriaIdOportunidad = (Integer) detalleMap.get("idOportunidad");
                        if (auditoriaIdUsuario != null && auditoriaIdUsuario.equals(idUsuario) &&
                            auditoriaIdOportunidad != null && auditoriaIdOportunidad.equals(idOportunidad)) {
                            return Optional.of(auditoria);
                        }
                    } else if (idConvenio != null) {
                        Integer auditoriaIdConvenio = (Integer) detalleMap.get("idConvenio");
                        if (auditoriaIdUsuario != null && auditoriaIdUsuario.equals(idUsuario) &&
                            auditoriaIdConvenio != null && auditoriaIdConvenio.equals(idConvenio)) {
                            return Optional.of(auditoria);
                        }
                    }
                } catch (Exception e) {
                    // Si falla el parsing de un registro, continuar con el siguiente
                    continue;
                }
            }

            return Optional.empty();
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar invitacion existente", e);
        }
    }
}
