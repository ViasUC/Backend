package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Endorsement;
import com.vias.uc.backend.model.Endorsement.Status;
import com.vias.uc.backend.repository.EndorsementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

import static com.vias.uc.backend.model.Endorsement.Status.*;

@Service
@RequiredArgsConstructor
public class EndorsementService {

    private final EndorsementRepository repo;
    private final AuditoriaService auditoria;
    private final UsuarioService usuarios;

    public Endorsement create(int fromId, int toId, String skill, String message) {
        if (fromId == toId)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes endorsarte");

        // --- reglas de existencia/estado/rol ---
        if (!usuarios.exists(fromId) || !usuarios.exists(toId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuario inexistente");

        if (!usuarios.activo(fromId) || !usuarios.activo(toId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuario inactivo");

        String rolFrom = usuarios.rol(fromId); // DOCENTE, EMPRESARIO, ALUMNO, etc.
        if (rolFrom == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol no definido");

        // ejemplo de política: solo DOCENTE o EMPRESARIO pueden avalar
        if (!rolFrom.matches("profesor|investigador|EMPLEADOR"))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Rol no autorizado para avalar");

        // anti-duplicado PENDING por (from,to,skill) (match con índice parcial de BD)
        String normSkill = (skill == null) ? "" : skill;
        if (repo.existsByFromUserIdAndToUserIdAndSkillAndStatus(fromId, toId, normSkill, PENDING))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un endorsement pendiente");

        Endorsement e = new Endorsement();
        e.setFromUserId(fromId);
        e.setToUserId(toId);
        e.setSkill(skill);
        e.setMessage(message);
        e.setStatus(PENDING);
        e.setCreatedAt(Instant.now());
        e.setIdAuditoria(auditoria.log(fromId, "endorsement.create", "to="+toId));

        try {
            return repo.save(e);
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Violación de FK/Auditoría: " + ex.getMostSpecificCause().getMessage());
        }
    }

    public Endorsement decide(long id, int actorId, boolean accept) {
        Endorsement e = repo.findByIdEndorsementAndToUserId(id, actorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo el receptor puede decidir"));
        if (e.getStatus() != PENDING) return e;

        e.setStatus(accept ? ACCEPTED : REJECTED);
        e.setDecidedAt(Instant.now());
        e.setIdAuditoria(auditoria.log(actorId, accept ? "endorsement.accept" : "endorsement.reject", "id="+id));

        return repo.save(e);
    }

    public List<Endorsement> inbox(int toUserId, Status status) {
        return (status == null) ? repo.findAllByToUserId(toUserId)
                : repo.findAllByToUserIdAndStatus(toUserId, status);
    }

    public List<Endorsement> given(int fromUserId) {
        return repo.findAllByFromUserId(fromUserId);
    }

    public List<Endorsement> publicForUser(int userId) {
        return repo.findAllByToUserIdAndStatus(userId, ACCEPTED);
    }
}
