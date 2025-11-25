package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Investigador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvestigadorRepository extends JpaRepository<Investigador, Integer> {
    boolean existsByIdUsuario(Integer idUsuario);
    Investigador findByUsuario_IdUsuario(Integer idUsuario);
}
