package com.vias.uc.backend.repository.canales;

import com.vias.uc.backend.model.canales.CanalPublicacion;
import com.vias.uc.backend.model.canales.CanalPublicacionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CanalPublicacionRepository extends JpaRepository<CanalPublicacion, CanalPublicacionId> {

    List<CanalPublicacion> findAllByCanal_IdCanalOrderByPublicacion_FechaPublicacionDesc(Integer idCanal);
    List<CanalPublicacion> findAllByCanal_IdCanalInOrderByPublicacion_FechaPublicacionDesc(List<Integer> ids);
    List<CanalPublicacion> findByCanal_IdCanalIn(List<Integer> idsCanales);
    List<CanalPublicacion> findByPublicacion_IdPublicacion(Integer idPublicacion);

    // Buscar la relación entre un canal y una publicación, para obtener el campo 'destacado'
    CanalPublicacion findByCanal_IdCanalAndPublicacion_IdPublicacion(Integer idCanal, Integer idPublicacion);
}
