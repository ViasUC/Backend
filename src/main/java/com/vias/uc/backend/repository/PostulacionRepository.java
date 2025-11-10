package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostulacionRepository extends JpaRepository<Postulacion, Integer> {
    boolean existsByIdPostulanteAndIdOportunidad(Integer idPostulante, Integer idOportunidad);
    Optional<Postulacion> findByIdPostulacionAndIdPostulante(Integer idPostulacion, Integer idPostulante);
}
