package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.model.Postulacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PostulacionRepository extends JpaRepository<Postulacion, Integer>, JpaSpecificationExecutor<Postulacion> {

    @EntityGraph(value = "Postulacion.graph", type = EntityGraph.EntityGraphType.LOAD)
    List<Postulacion> findByAlumno(Alumno alumno);

    @EntityGraph(value = "Postulacion.graph", type = EntityGraph.EntityGraphType.LOAD)
    List<Postulacion> findByOportunidad(Oportunidad oportunidad);

    // Alumno tiene PK = idUsuario (Long). Oportunidad usa Integer.
    boolean existsByAlumno_IdUsuarioAndOportunidad_IdOportunidad(Long idUsuario, Integer idOportunidad);

    @Override
    @EntityGraph(value = "Postulacion.graph", type = EntityGraph.EntityGraphType.LOAD)
    Page<Postulacion> findAll(Specification<Postulacion> spec, Pageable pageable);
}
