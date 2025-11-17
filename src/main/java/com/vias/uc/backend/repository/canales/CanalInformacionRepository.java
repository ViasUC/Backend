package com.vias.uc.backend.repository.canales;

import com.vias.uc.backend.model.canales.CanalInformacion;
import com.vias.uc.backend.model.enums.TipoCanal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CanalInformacionRepository extends JpaRepository<CanalInformacion, Integer> {

    List<CanalInformacion> findAllByActivoTrue();

    Optional<CanalInformacion> findBySlugAndActivoTrue(String slug);

    List<CanalInformacion> findByTipo(String tipo);

    List<CanalInformacion> findByTipo(TipoCanal tipo);

}
