package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostulacionRepository extends JpaRepository<Postulacion, Integer> {

    // Todas las postulaciones hechas por un usuario
    List<Postulacion> findByIdPostulante(Integer idPostulante);

    // Todas las postulaciones realizadas sobre una oportunidad
    List<Postulacion> findByIdOportunidad(Integer idOportunidad);

    // Una postulación específica (para despostular)
    Postulacion findByIdPostulanteAndIdOportunidad(Integer idPostulante, Integer idOportunidad);

    // También podés borrar directo sin buscarla antes
    void deleteByIdPostulanteAndIdOportunidad(Integer idPostulante, Integer idOportunidad);
}
