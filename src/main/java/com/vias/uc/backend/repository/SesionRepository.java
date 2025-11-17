package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, Integer> {
    
    /**
     * Busca todas las sesiones de un usuario
     */
    List<Sesion> findByIdUsuario(Integer idUsuario);
    
    /**
     * Busca las sesiones activas de un usuario (fecha_fin es null)
     */
    List<Sesion> findByIdUsuarioAndFechaFinIsNull(Integer idUsuario);
    
    /**
     * Busca la sesión activa más reciente de un usuario
     */
    Optional<Sesion> findFirstByIdUsuarioAndFechaFinIsNullOrderByFechaIniDesc(Integer idUsuario);
}
