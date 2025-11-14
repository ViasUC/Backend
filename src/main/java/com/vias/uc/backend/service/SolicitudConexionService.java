package com.vias.uc.backend.service;

import com.vias.uc.backend.model.SolicitudConexion;
import com.vias.uc.backend.model.Conexion;
import com.vias.uc.backend.repository.SolicitudConexionRepository;
import com.vias.uc.backend.repository.ConexionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudConexionService {

    private final SolicitudConexionRepository repo;
    private final ConexionRepository conexionRepo;

    public SolicitudConexion enviarSolicitud(Integer origen, Integer destino) {
        SolicitudConexion s = new SolicitudConexion();
        s.setIdUsuarioOrigen(origen);
        s.setIdUsuarioDestino(destino);
        s.setEstado("pendiente");
        s.setFechaSolicitud(LocalDate.now());
        return repo.save(s);
    }

    public SolicitudConexion aceptarSolicitud(Integer idSolicitud) {

        SolicitudConexion s = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        s.setEstado("aceptada");
        s.setFechaRespuesta(LocalDate.now());
        repo.save(s);

        // crear conexion
        Conexion c = new Conexion();
        c.setIdUsuario1(s.getIdUsuarioOrigen());
        c.setIdUsuario2(s.getIdUsuarioDestino());
        c.setFechaConexion(LocalDate.now());
        conexionRepo.save(c);

        return s;
    }

    public Boolean rechazarSolicitud(Integer idSolicitud) {

        SolicitudConexion s = repo.findById(idSolicitud)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        s.setEstado("rechazada");
        s.setFechaRespuesta(LocalDate.now());
        repo.save(s);

        return true;
    }

    public List<SolicitudConexion> recibidas(Integer idUsuario) {
        return repo.findByIdUsuarioDestino(idUsuario);
    }

    public List<SolicitudConexion> enviadas(Integer idUsuario) {
        return repo.findByIdUsuarioOrigen(idUsuario);
    }

    public List<SolicitudConexion> pendientes(Integer idUsuario) {
        return repo.findByIdUsuarioDestinoAndEstado(idUsuario, "pendiente");
    }
}
