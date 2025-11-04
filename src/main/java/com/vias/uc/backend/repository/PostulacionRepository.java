package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Postulacion;
import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Oportunidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PostulacionRepository extends JpaRepository<Postulacion, Long>, JpaSpecificationExecutor<Postulacion> {
    List<Postulacion> findByAlumno(Alumno alumno);
    List<Postulacion> findByOportunidad(Oportunidad oportunidad);
}
