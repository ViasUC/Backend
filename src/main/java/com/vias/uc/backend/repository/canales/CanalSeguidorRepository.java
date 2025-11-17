package com.vias.uc.backend.repository.canales;

import com.vias.uc.backend.model.canales.CanalSeguidor;
import com.vias.uc.backend.model.canales.CanalSeguidorId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface CanalSeguidorRepository extends JpaRepository<CanalSeguidor, CanalSeguidorId> {

    List<CanalSeguidor> findAllById_IdUsuario(Integer idUsuario);
    boolean existsById(CanalSeguidorId id);

    Optional<CanalSeguidor> findByCanal_IdCanalAndUsuario_IdUsuario(Integer idCanal, Integer idUsuario);
    void deleteByCanal_IdCanalAndUsuario_IdUsuario(Integer idCanal, Integer idUsuario);
}
