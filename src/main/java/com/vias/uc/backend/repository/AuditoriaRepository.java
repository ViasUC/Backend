package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Integer> {

    @Query("SELECT COALESCE(MAX(a.idAuditoria), 0) FROM Auditoria a")
    Integer getUltimoId();
}
