package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Postulacion;
import com.vias.uc.backend.model.PostulacionEvidencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostulacionEvidenciaRepository extends JpaRepository<PostulacionEvidencia, Long> {

    List<PostulacionEvidencia> findByPostulacion(Postulacion postulacion);

    // opcional si querés buscar por id directamente
    List<PostulacionEvidencia> findByPostulacion_IdPostulacion(Long idPostulacion);
}
