package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.HistorialPostulacion;
import com.vias.uc.backend.model.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialPostulacionRepository extends JpaRepository<HistorialPostulacion, Integer> {
    List<HistorialPostulacion> findByPostulacionOrderByFechaCambioDesc(Postulacion postulacion);
}
