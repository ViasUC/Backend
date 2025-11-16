package com.vias.uc.backend.graphql.mutation;

import com.vias.uc.backend.model.Postulacion;
import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.repository.PostulacionRepository;
import com.vias.uc.backend.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class PostulacionMutation {

    private final PostulacionRepository repo;
    private final AuditoriaRepository auditoriaRepo;

    // ==========================================================
    // 🔵 CREAR POSTULACIÓN (YA TENÍAS)
    // ==========================================================
    @MutationMapping
    public Postulacion crearPostulacion(
            @Argument Integer idOportunidad,
            @Argument Integer idPostulante,
            @Argument Integer idOfertante
    ) {

        System.out.println("📩 Crear postulacion:");
        System.out.println(" - idOportunidad = " + idOportunidad);
        System.out.println(" - idPostulante  = " + idPostulante);
        System.out.println(" - idOfertante   = " + idOfertante);

        // Auditoría
        Auditoria au = new Auditoria();
        au.setActorId(idPostulante);
        au.setAccion("POSTULACION");
        au.setDetalle("El usuario " + idPostulante + " postuló a " + idOportunidad);
        au = auditoriaRepo.save(au);

        Postulacion p = new Postulacion();
        p.setIdOportunidad(idOportunidad);
        p.setIdPostulante(idPostulante);
        p.setIdOfertante(idOfertante);
        p.setEstado("PENDIENTE");
        p.setFechaPostulacion(LocalDateTime.now());
        p.setMotivo(null);
        p.setIdAuditoria(au.getIdAuditoria());

        return repo.save(p);
    }

    // ==========================================================
    // 🔴 ELIMINAR POSTULACIÓN (NUEVO — NO ROMPE NADA)
    // ==========================================================
    @MutationMapping
    public Boolean eliminarPostulacion(
            @Argument Integer idPostulante,
            @Argument Integer idOportunidad
    ) {

        System.out.println("🗑 Eliminando postulación...");
        System.out.println(" - idPostulante  = " + idPostulante);
        System.out.println(" - idOportunidad = " + idOportunidad);

        // Buscar la postulación exacta
        Postulacion p = repo.findByIdPostulanteAndIdOportunidad(idPostulante, idOportunidad);

        if (p == null) {
            System.out.println("❌ No se encontró la postulación");
            return false;
        }

        // Auditoría
        Auditoria au = new Auditoria();
        au.setActorId(idPostulante);
        au.setAccion("CANCELAR_POSTULACION");
        au.setDetalle("El usuario " + idPostulante + " canceló su postulación a " + idOportunidad);
        auditoriaRepo.save(au);

        repo.delete(p);

        System.out.println("✔ Postulación eliminada correctamente");
        return true;
    }
}
