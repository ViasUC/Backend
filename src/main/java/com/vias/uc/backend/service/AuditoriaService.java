package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditoriaService {
    private final AuditoriaRepository repo;

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
}
