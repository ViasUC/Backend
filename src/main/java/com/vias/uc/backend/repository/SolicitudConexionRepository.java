package com.vias.uc.backend.repository;

import com.vias.uc.backend.model.SolicitudConexion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitudConexionRepository extends JpaRepository<SolicitudConexion, Integer> {

    List<SolicitudConexion> findByIdUsuarioDestino(Integer destino);

    List<SolicitudConexion> findByIdUsuarioOrigen(Integer origen);

    List<SolicitudConexion> findByIdUsuarioDestinoAndEstado(Integer destino, String estado);

    // ⭐ NECESARIO para evitar solicitudes duplicadas
    List<SolicitudConexion> findByIdUsuarioOrigenAndIdUsuarioDestinoAndEstado(
            Integer origen,
            Integer destino,
            String estado
    );
}
