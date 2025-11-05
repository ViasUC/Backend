package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Alumno;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {

    @EntityGraph(attributePaths = {"usuario"})
    Optional<Alumno> findById(Integer id);

    boolean existsById(Integer id);
}
