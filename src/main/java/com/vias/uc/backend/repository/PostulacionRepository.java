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

public interface PostulacionRepository extends JpaRepository<Postulacion, Long>, JpaSpecificationExecutor<Postulacion> {

    // Para listas simples (evita LazyInitialization al leer campos del postulante y de la oportunidad)
    @EntityGraph(attributePaths = { "postulante", "oportunidad" })
    List<Postulacion> findByAlumno(Alumno alumno);

    @EntityGraph(attributePaths = { "postulante", "oportunidad" })
    List<Postulacion> findByOportunidad(Oportunidad oportunidad);

    boolean existsByAlumno_IdAlumnoAndOportunidad_IdOportunidad(Integer idAlumno, Integer idOportunidad);

    // Sobrescribe el findAll con Specification para que traiga las relaciones necesarias en la paginación
    @Override
    @EntityGraph(attributePaths = { "postulante", "oportunidad" })
    Page<Postulacion> findAll(Specification<Postulacion> spec, Pageable pageable);
}
