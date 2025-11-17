package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfesorRepository extends JpaRepository<Profesor, Integer> {
    boolean existsByIdUsuario(Integer idUsuario);
}
