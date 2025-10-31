package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.HistorialPostulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialPostulacionRepository extends JpaRepository<HistorialPostulacion, Long> {
    List<HistorialPostulacion> findByPostulacion_IdPostulacionOrderByFechaCambioAsc(Long idPostulacion);
}
