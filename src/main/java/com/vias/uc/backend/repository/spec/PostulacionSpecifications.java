package com.vias.uc.backend.repository.spec;

import com.vias.uc.backend.model.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class PostulacionSpecifications {

    public static Specification<Postulacion> porOportunidad(Oportunidad oportunidad) {
        return (root, query, cb) ->
                oportunidad == null ? null : cb.equal(root.get("oportunidad"), oportunidad);
    }

    // REEMPLAZA porAlumno(...) por este:
    public static Specification<Postulacion> porPostulante(Usuario postulante) {
        return (root, query, cb) ->
                postulante == null ? null : cb.equal(root.get("postulante"), postulante);
    }

    public static Specification<Postulacion> porOfertante(Usuario ofertante) {
        return (root, query, cb) -> {
            if (ofertante == null) return null;
            return cb.equal(root.get("ofertante"), ofertante);
        };
    }

    public static Specification<Postulacion> porEstados(List<EstadoPostulacion> estados) {
        return (root, query, cb) ->
                (estados == null || estados.isEmpty()) ? null : root.get("estado").in(estados);
    }

    public static Specification<Postulacion> desde(LocalDateTime desde) {
        return (root, query, cb) ->
                desde == null ? null : cb.greaterThanOrEqualTo(root.get("fechaPostulacion"), desde);
    }

    public static Specification<Postulacion> hasta(LocalDateTime hasta) {
        return (root, query, cb) ->
                hasta == null ? null : cb.lessThanOrEqualTo(root.get("fechaPostulacion"), hasta);
    }

    // Búsqueda textual simple (en campos típicos; ajusta si tenés otros)
    public static Specification<Postulacion> texto(String texto) {
        return (root, query, cb) -> {
            if (texto == null || texto.isBlank()) return null;
            String like = "%" + texto.trim().toLowerCase() + "%";
            // ejemplo sobre motivo; agrega más joins/fields si querés
            return cb.like(cb.lower(root.get("motivo")), like);
        };
    }
}
