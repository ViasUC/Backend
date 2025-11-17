package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.model.Postulacion;
import com.vias.uc.backend.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PostulacionRepository extends JpaRepository<Postulacion, Integer>, JpaSpecificationExecutor<Postulacion> {

    @EntityGraph(value = "Postulacion.graph", type = EntityGraph.EntityGraphType.LOAD)
    List<Postulacion> findByPostulante(Usuario postulante);

    @EntityGraph(value = "Postulacion.graph", type = EntityGraph.EntityGraphType.LOAD)
    List<Postulacion> findByOportunidad(Oportunidad oportunidad);

    // Alumno tiene PK = idUsuario (Long). Oportunidad usa Integer.
    boolean existsByPostulante_IdUsuarioAndOportunidad_IdOportunidad(Integer idUsuario, Integer idOportunidad);

    @Override
    @EntityGraph(value = "Postulacion.graph", type = EntityGraph.EntityGraphType.LOAD)
    Page<Postulacion> findAll(Specification<Postulacion> spec, Pageable pageable);
}
