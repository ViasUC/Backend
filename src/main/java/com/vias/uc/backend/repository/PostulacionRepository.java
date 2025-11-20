package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PostulacionRepository extends JpaRepository<Postulacion, Integer> {

    // Todas las postulaciones hechas por un usuario
    List<Postulacion> findByIdPostulante(Integer idPostulante);

    // Todas las postulaciones realizadas sobre una oportunidad
    List<Postulacion> findByIdOportunidad(Integer idOportunidad);

    // Una postulación específica (para despostular)
    Postulacion findByIdPostulanteAndIdOportunidad(Integer idPostulante, Integer idOportunidad);

    // Borrar directo
    void deleteByIdPostulanteAndIdOportunidad(Integer idPostulante, Integer idOportunidad);

    @Modifying
    @Transactional
    @Query("UPDATE Postulacion p SET p.estado = :estado WHERE p.idPostulante = :idPostulante AND p.idOportunidad = :idOportunidad")
    int updateEstado(Integer idPostulante, Integer idOportunidad, String estado);
}
