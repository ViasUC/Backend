package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    List<Curso> findByArea(String area);
    List<Curso> findByModalidad(String modalidad);
    List<Curso> findByAreaAndModalidad(String area, String modalidad);
}
