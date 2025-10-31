package com.vias.uc.backend.resolver;

import com.vias.uc.backend.model.Postulacion;
import com.vias.uc.backend.model.HistorialPostulacion;
import com.vias.uc.backend.repository.HistorialPostulacionRepository;
import com.vias.uc.backend.service.PostulacionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PostulacionResolver {

    private final PostulacionService postulacionService;
    private final HistorialPostulacionRepository historialRepo;

    public PostulacionResolver(PostulacionService postulacionService,
                               HistorialPostulacionRepository historialRepo) {
        this.postulacionService = postulacionService;
        this.historialRepo = historialRepo;
    }

    record PageableInput(Integer page, Integer size, String sort) {}
    record PostulacionFilter(List<Postulacion.EstadoPostulacion> estados, String q) {}

    @QueryMapping
    public List<Postulacion> postulaciones() {
        return postulacionService.listarPorOportunidad(null, null, null, PageRequest.of(0, 100)).getContent();
    }

    @QueryMapping
    public Object postulacionesPorOportunidad(@Argument Long oportunidadId,
                                              @Argument PostulacionFilter filter,
                                              @Argument PageableInput page) {
        var estado = (filter != null && filter.estados() != null && !filter.estados().isEmpty())
                ? filter.estados().getFirst() : null;
        var pageable = PageRequest.of(
                page != null && page.page() != null ? page.page() : 0,
                page != null && page.size() != null ? page.size() : 20,
                (page != null && page.sort() != null) ? Sort.by(page.sort().split(",")) : Sort.by("idPostulacion").descending()
        );

        var pageResult = postulacionService.listarPorOportunidad(oportunidadId, estado, filter != null ? filter.q() : null, pageable);
        return new Object() {
            public List<Postulacion> content = pageResult.getContent();
            public long totalElements = pageResult.getTotalElements();
            public int totalPages = pageResult.getTotalPages();
        };
    }

    @MutationMapping
    public Postulacion crearPostulacion(@Argument Long usuarioId,
                                        @Argument Long oportunidadId,
                                        @Argument String mensaje,
                                        @Argument String curriculumUrl) {
        return postulacionService.crear(usuarioId, oportunidadId, mensaje, curriculumUrl);
    }

    @MutationMapping
    public Postulacion actualizarEstadoPostulacion(@Argument Long idPostulacion,
                                                   @Argument Postulacion.EstadoPostulacion estado,
                                                   @Argument String motivo) {
        return postulacionService.actualizarEstado(idPostulacion, estado, motivo);
    }

    @SchemaMapping(typeName = "Postulacion", field = "historial")
    public List<HistorialPostulacion> historial(Postulacion p) {
        return historialRepo.findByPostulacion_IdPostulacionOrderByFechaCambioAsc(p.getIdPostulacion());
    }
}
