package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Alumno;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    @EntityGraph(attributePaths = {"usuario"})
    Optional<Alumno> findById(Long id);

    boolean existsById(Long id);

    /**
     * Obtiene todas las carreras únicas de la base de datos
     */
    @Query("SELECT DISTINCT a.carrera FROM Alumno a WHERE a.carrera IS NOT NULL ORDER BY a.carrera")
    List<String> findDistinctCarreras();
}
