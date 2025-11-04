package com.vias.uc.backend.repository.spec;

import com.vias.uc.backend.model.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class PostulacionSpecifications {

    public static Specification<Postulacion> porOportunidad(Oportunidad op) {
        return (root, q, cb) -> op == null ? null : cb.equal(root.get("oportunidad"), op);
    }

    public static Specification<Postulacion> porAlumno(Alumno alumno) {
        return (root, q, cb) -> alumno == null ? null : cb.equal(root.get("alumno"), alumno);
    }

    public static Specification<Postulacion> porEstados(List<EstadoPostulacion> estados) {
        return (root, q, cb) ->
                (estados == null || estados.isEmpty()) ? null : root.get("estado").in(estados);
    }

    public static Specification<Postulacion> desde(LocalDateTime desde) {
        return (root, q, cb) ->
                desde == null ? null : cb.greaterThanOrEqualTo(root.get("fechaPostulacion"), desde);
    }

    public static Specification<Postulacion> hasta(LocalDateTime hasta) {
        return (root, q, cb) ->
                hasta == null ? null : cb.lessThanOrEqualTo(root.get("fechaPostulacion"), hasta);
    }

    // busca en titulo de la oportunidad o nombre del postulante
    public static Specification<Postulacion> texto(String texto) {
        return (root, q, cb) -> {
            if (texto == null || texto.isBlank()) return null;
            String like = "%" + texto.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("oportunidad").get("titulo")), like),
                    cb.like(cb.lower(root.get("postulante").get("nombre")), like)
            );
        };
    }
}
