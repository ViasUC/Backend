package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvidenciaRepository extends JpaRepository<Evidencia, Integer> {

    List<Evidencia> findByIdPortafolio(Integer idPortafolio);
}
