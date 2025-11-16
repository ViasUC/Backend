package com.vias.uc.backend.graphql.mutation;

import com.vias.uc.backend.model.Auditoria;
import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.repository.AuditoriaRepository;
import com.vias.uc.backend.repository.OportunidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class OportunidadMutation {

    private final OportunidadRepository repo;
    private final AuditoriaRepository auditoriaRepo;

    // ============================================================
    // 🔥 CREAR OPORTUNIDAD
    // ============================================================
    @MutationMapping
    public Oportunidad crearOportunidad(
            @Argument Integer idEmpresa,
            @Argument Integer idCreador,
            @Argument String titulo,
            @Argument String descripcion,
            @Argument String requisitos,
            @Argument String ubicacion,
            @Argument String modalidad,
            @Argument String tipo,
            @Argument String fechaPublicacion,
            @Argument String fechaCierre,
            @Argument String estado
    ) {

        // Auditoría
        Auditoria au = new Auditoria();
        au.setActorId(idCreador);
        au.setAccion("CREAR_OPORTUNIDAD");
        au.setDetalle("Creación de oportunidad: " + titulo);
        au = auditoriaRepo.save(au);

        // Crear entidad
        Oportunidad op = new Oportunidad();
        op.setIdEmpresa(idEmpresa);
        op.setIdCreador(idCreador);
        op.setTitulo(titulo);
        op.setDescripcion(descripcion);
        op.setRequisitos(requisitos);
        op.setUbicacion(ubicacion);
        op.setModalidad(modalidad);
        op.setTipo(tipo);

        if (fechaPublicacion != null && !fechaPublicacion.isBlank())
            op.setFechaPublicacion(LocalDateTime.parse(fechaPublicacion));

        if (fechaCierre != null && !fechaCierre.isBlank())
            op.setFechaCierre(LocalDateTime.parse(fechaCierre));

        op.setEstado(estado);
        op.setIdAuditoria(au.getIdAuditoria());

        return repo.save(op);
    }
// ============================================================
// 🔥 ELIMINAR OPORTUNIDAD (CON LOGS EXAGERADOS)
// ============================================================
@MutationMapping
public Boolean eliminarOportunidad(@Argument Integer idOportunidad) {

    System.out.println("==========================================");
    System.out.println("🔥🔥🔥 ENTRÓ A eliminarOportunidad()");
    System.out.println("🗑️ ID recibido = " + idOportunidad);
    System.out.println("==========================================");

    boolean exists = repo.existsById(idOportunidad);
    System.out.println("¿Existe en la BD?: " + exists);

    if (!exists) {
        System.out.println("❌ No existe la oportunidad. Abortando.");
        return false;
    }

    try {
        repo.deleteById(idOportunidad);
        System.out.println("✔ Eliminada correctamente en BD");
        return true;

    } catch (Exception e) {
        System.out.println("❌ ERROR eliminando:");
        e.printStackTrace();
        return false;
    }
}

    // ============================================================
    // 🔥 ACTUALIZAR OPORTUNIDAD
    // ============================================================
    @MutationMapping
    public Oportunidad actualizarOportunidad(
            @Argument Integer idOportunidad,
            @Argument Integer idCreador,
            @Argument String titulo,
            @Argument String descripcion,
            @Argument String requisitos,
            @Argument String ubicacion,
            @Argument String modalidad,
            @Argument String tipo,
            @Argument String fechaCierre,
            @Argument String estado
    ) {

        // Buscar existente
        Oportunidad op = repo.findById(idOportunidad)
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada"));

        // Auditoría
        Auditoria au = new Auditoria();
        au.setActorId(idCreador);
        au.setAccion("EDITAR_OPORTUNIDAD");
        au.setDetalle("Edición de oportunidad: " + titulo);
        au = auditoriaRepo.save(au);

        // Actualizar campos
        op.setTitulo(titulo);
        op.setDescripcion(descripcion);
        op.setRequisitos(requisitos);
        op.setUbicacion(ubicacion);
        op.setModalidad(modalidad);
        op.setTipo(tipo);

        if (fechaCierre != null && !fechaCierre.isBlank())
            op.setFechaCierre(LocalDateTime.parse(fechaCierre));

        op.setEstado(estado);
        op.setIdAuditoria(au.getIdAuditoria());

        return repo.save(op);
    }
}
