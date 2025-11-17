package com.vias.uc.backend.graphql.mutation;

import com.vias.uc.backend.graphql.dto.CrearEvidenciaInput;
import com.vias.uc.backend.graphql.dto.EditarEvidenciaInput;
import com.vias.uc.backend.model.Evidencia;
import com.vias.uc.backend.repository.EvidenciaRepository;
import com.vias.uc.backend.repository.PortafolioRepository;
import com.vias.uc.backend.service.AuditoriaService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

@Controller
public class EvidenciaMutation {

    private final EvidenciaRepository evRepo;
    private final PortafolioRepository portRepo;
    private final AuditoriaService auditoriaService;


    public EvidenciaMutation(EvidenciaRepository evRepo,
                             PortafolioRepository portRepo,
                             AuditoriaService auditoriaService) {
        this.evRepo = evRepo;
        this.portRepo = portRepo;
        this.auditoriaService = auditoriaService;
    }


    // Crear evidencia
    @MutationMapping
    @Transactional
    public Evidencia agregarEvidencia(@Argument CrearEvidenciaInput input) {

        var port = portRepo.findById(input.getIdPortafolio())
                .orElseThrow(() -> new RuntimeException("Portafolio inexistente"));

        // VALIDACIÓN: El usuario debe ser el dueño del portafolio
        if (!port.getIdUsuario().equals(input.getIdUsuario())) {
            throw new RuntimeException("No puedes agregar evidencias al portafolio de otro usuario");
        }

        var ev = new Evidencia();
        ev.setIdPortafolio(input.getIdPortafolio());
        ev.setTitulo(input.getTitulo());
        ev.setDescripcion(input.getDescripcion());
        ev.setTipo(input.getTipo());
        ev.setRecurso(input.getRecurso());

        // 🔥 AUDITORÍA
        Integer auditId = auditoriaService.log(
                port.getIdUsuario(),
                "AGREGAR_EVIDENCIA",
                "Evidencia agregada al portafolio " + port.getIdPortafolio()
        );
        ev.setIdAuditoria(auditId);

        return evRepo.save(ev);
    }


    // Editar evidencia
    @MutationMapping
    @Transactional
    public Evidencia editarEvidencia(@Argument EditarEvidenciaInput input) {

        var ev = evRepo.findById(input.getIdEvidencia())
                .orElseThrow(() -> new RuntimeException("Evidencia no encontrada"));

        var port = portRepo.findById(ev.getIdPortafolio())
                .orElseThrow(() -> new RuntimeException("Portafolio inconsistente"));

        // VALIDACIÓN: Solo el dueño puede editar
        if (!port.getIdUsuario().equals(input.getIdUsuario())) {
            throw new RuntimeException("No puedes editar evidencias que no te pertenecen");
        }

        if (input.getTitulo() != null) ev.setTitulo(input.getTitulo());
        if (input.getDescripcion() != null) ev.setDescripcion(input.getDescripcion());
        if (input.getTipo() != null) ev.setTipo(input.getTipo());
        if (input.getRecurso() != null) ev.setRecurso(input.getRecurso());

        Integer auditId = auditoriaService.log(
                port.getIdUsuario(),
                "EDITAR_EVIDENCIA",
                "Evidencia editada: " + ev.getIdEvidencia()
        );
        ev.setIdAuditoria(auditId);

        return evRepo.save(ev);
    }


    // Eliminar evidencia
    @MutationMapping
    @Transactional
    public Boolean eliminarEvidencia(@Argument Integer idEvidencia, @Argument Integer idUsuario) {

        var ev = evRepo.findById(idEvidencia)
                .orElseThrow(() -> new RuntimeException("Evidencia no existe"));

        var port = portRepo.findById(ev.getIdPortafolio())
                .orElseThrow(() -> new RuntimeException("Portafolio inconsistente"));

        // VALIDACIÓN: Solo el dueño puede eliminar
        if (!port.getIdUsuario().equals(idUsuario)) {
            throw new RuntimeException("No puedes eliminar evidencias que no te pertenecen");
        }

        auditoriaService.log(
                port.getIdUsuario(),
                "ELIMINAR_EVIDENCIA",
                "Evidencia eliminada: " + idEvidencia
        );

        evRepo.delete(ev);
        return true;
    }


}
