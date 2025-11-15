package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Publicacion;
import com.vias.uc.backend.model.enums.EstadoPublicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicacionRepository extends JpaRepository<Publicacion, Integer> {

    //List<Publicacion> findAllByPublicadoPorProfesor(Integer idProfesor);

    //List<Publicacion> findAllByEstado(EstadoPublicacion estado);
    List<Publicacion> findAllByIdPublicacionInOrderByFechaPublicacionDesc(List<Integer> idsPublicaciones);

}
