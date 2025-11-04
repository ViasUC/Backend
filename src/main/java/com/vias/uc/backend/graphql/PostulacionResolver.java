package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.*;
import com.vias.uc.backend.service.PostulacionService;
import org.springframework.data.domain.*;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PostulacionResolver {

    private final PostulacionService postulacionService;

    public PostulacionResolver(PostulacionService postulacionService) {
        this.postulacionService = postulacionService;
    }

    // ===== Existentes =====
    @QueryMapping
    public List<Postulacion> postulaciones() {
        return postulacionService.listarTodas();
    }

    @QueryMapping
    public List<Postulacion> postulacionesPorAlumno(@Argument Long idAlumno) {
        return postulacionService.listarPorAlumno(idAlumno);
    }

    @QueryMapping
    public List<Postulacion> postulacionesPorOportunidad(@Argument Long idOportunidad) {
        return postulacionService.listarPorOportunidad(idOportunidad);
    }

    @MutationMapping
    public Postulacion crearPostulacion(@Argument Long idAlumno, @Argument Long idOportunidad) {
        return postulacionService.crearPostulacion(idAlumno, idOportunidad);
    }

    // ===== F1: filtros/paginación =====
    public record FiltroInput(Long idOportunidad, Long idAlumno, List<EstadoPostulacion> estados,
                              String fechaDesde, String fechaHasta, String texto) {}

    @QueryMapping
    public com.vias.uc.backend.graphql.dto.PostulacionPageDTO postulacionesPage(
            @Argument FiltroInput filtro,
            @Argument int page,
            @Argument int size,
            @Argument String sort) {

        Sort orden = Sort.by(Sort.Order.desc("fechaPostulacion"));
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            String prop = parts[0];
            boolean asc = parts.length < 2 || !"desc".equalsIgnoreCase(parts[1]);
            orden = asc ? Sort.by(prop).ascending() : Sort.by(prop).descending();
        }
        Pageable pageable = PageRequest.of(page, size, orden);

        Long idOportunidad = filtro != null ? filtro.idOportunidad() : null;
        Long idAlumno = filtro != null ? filtro.idAlumno() : null;
        List<EstadoPostulacion> estados = filtro != null ? filtro.estados() : null;
        String desde = filtro != null ? filtro.fechaDesde() : null;
        String hasta = filtro != null ? filtro.fechaHasta() : null;
        String texto = filtro != null ? filtro.texto() : null;

        Page<Postulacion> res = postulacionService.buscarConFiltros(
                idOportunidad, idAlumno, estados, desde, hasta, texto, pageable);

        return new com.vias.uc.backend.graphql.dto.PostulacionPageDTO(
                res.getContent(), (int) res.getTotalElements(), res.getNumber(), res.getSize()
        );
    }

    // ===== F1: historial =====
    @QueryMapping
    public List<HistorialPostulacion> historialPostulacion(@Argument Long idPostulacion) {
        return postulacionService.historial(idPostulacion);
    }

    // ===== F1: cambio de estado =====
    @MutationMapping
    public Postulacion actualizarEstadoPostulacion(@Argument Long idPostulacion,
                                                   @Argument EstadoPostulacion estado,
                                                   @Argument String motivo,
                                                   @Argument Long idActor) {
        return postulacionService.actualizarEstado(idPostulacion, estado, motivo, idActor);
    }
}
