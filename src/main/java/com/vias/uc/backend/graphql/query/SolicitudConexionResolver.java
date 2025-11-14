package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.SolicitudConexion;
import com.vias.uc.backend.service.SolicitudConexionService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SolicitudConexionResolver {

    private final SolicitudConexionService solicitudService;

    // ============================================================
    // 🔵 PENDIENTES
    // ============================================================
    @QueryMapping
    public List<SolicitudConexion> solicitudesPendientes(@Argument Integer idUsuario) {
        return solicitudService.pendientes(idUsuario);
    }

    // ============================================================
    // 📨 RECIBIDAS
    // ============================================================
    @QueryMapping
    public List<SolicitudConexion> solicitudesRecibidas(@Argument Integer idUsuario) {
        return solicitudService.recibidas(idUsuario);
    }

    // ============================================================
    // 📨 ENVIADAS
    // ============================================================
    @QueryMapping
    public List<SolicitudConexion> solicitudesEnviadas(@Argument Integer idUsuario) {
        return solicitudService.enviadas(idUsuario);
    }

    // ============================================================
    // ✔ ACEPTAR
    // ============================================================
    @MutationMapping
    public SolicitudConexion aceptarSolicitud(@Argument Integer idSolicitud) {
        return solicitudService.aceptarSolicitud(idSolicitud);
    }

    // ============================================================
    // ❌ RECHAZAR
    // ============================================================
    @MutationMapping
    public Boolean rechazarSolicitud(@Argument Integer idSolicitud) {
        return solicitudService.rechazarSolicitud(idSolicitud);
    }
}
