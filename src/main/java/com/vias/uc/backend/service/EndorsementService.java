package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Endorsement;
import com.vias.uc.backend.repository.EndorsementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class EndorsementService {

    @Autowired
    private EndorsementRepository endorsementRepository;

    @Autowired
    private JdbcTemplate jdbc;

    @Transactional
    public Endorsement crearEndorsement(Integer emisorId, Integer receptorId) {

        // Crear registro en auditoría CON LA ESTRUCTURA REAL
        Integer newAuditId = jdbc.queryForObject(
            "INSERT INTO auditoria (actor_id, accion, detalle, fecha_evento) " +
            "VALUES (?, ?, ?, NOW()) RETURNING id_auditoria",
            Integer.class,
            emisorId,
            "ENDORSEMENT",
            "El usuario " + emisorId + " endorsó al usuario " + receptorId
        );

        // Crear objeto endorsement
        Endorsement e = new Endorsement();
        e.setIdUsuarioEmisor(emisorId);
        e.setIdUsuarioReceptor(receptorId);
        e.setFechaEndorsement(LocalDate.now());
        e.setComentario(null);
        e.setIdAuditoria(newAuditId);

        // Guardar en BD
        return endorsementRepository.save(e);
    }

    public List<Endorsement> obtenerRealizados(Integer idUsuario) {
        return endorsementRepository.findByIdUsuarioEmisor(idUsuario);
    }

    public List<Endorsement> obtenerRecibidos(Integer idUsuario) {
        return endorsementRepository.findByIdUsuarioReceptor(idUsuario);
    }

    @Transactional
    public boolean eliminarEndorsement(Integer idEndorsement) {
        if (endorsementRepository.existsById(idEndorsement)) {
            endorsementRepository.deleteById(idEndorsement);
            return true;
        }
        return false;
    }
}
