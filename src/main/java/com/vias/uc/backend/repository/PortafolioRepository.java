package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Portafolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortafolioRepository extends JpaRepository<Portafolio, Integer> {

    Optional<Portafolio> findByIdUsuario(Long idUsuario);
}
