package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface EvidenciaRepository extends JpaRepository<Evidencia, Integer> {

    // Trae todas las evidencias de un portafolio específico
    List<Evidencia> findByIdPortafolio(Integer idPortafolio);

    // ⚡ Cuenta cuántas evidencias de una lista pertenecen al portafolio indicado
    long countByIdEvidenciaInAndIdPortafolio(Collection<Integer> ids, Integer idPortafolio);
}
