package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.*;
import com.vias.uc.backend.service.PostulacionService;
import org.springframework.data.domain.*;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.security.access.AccessDeniedException;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.enums.RolUsuario;
import com.vias.uc.backend.graphql.dto.PostulacionPageDTO;

import com.vias.uc.backend.repository.PostulacionEvidenciaRepository; // NUEVO

import java.util.List;

@Controller
public class PostulacionResolver {

    private final PostulacionService postulacionService;
    private final UsuarioRepository usuarioRepository;
    private final PostulacionEvidenciaRepository postulacionEvidenciaRepository; // NUEVO

    public PostulacionResolver(PostulacionService postulacionService,
                               UsuarioRepository usuarioRepository,
                               PostulacionEvidenciaRepository postulacionEvidenciaRepository) { // NUEVO
        this.postulacionService = postulacionService;
        this.usuarioRepository = usuarioRepository;
        this.postulacionEvidenciaRepository = postulacionEvidenciaRepository;   // NUEVO
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
    public Postulacion crearPostulacion(@Argument Long idAlumno,
                                        @Argument Long idOportunidad,
                                        @Argument(name = "idsEvidencias") List<Integer> idsEvidencias) {
        // idsEvidencias puede venir null o vacío → vinculación opcional
        return postulacionService.crearPostulacion(idAlumno, idOportunidad, idsEvidencias);
    }

    // ===== Resolver de campo: Postulacion.evidencias =====
    @SchemaMapping(typeName = "Postulacion", field = "evidencias")
    public List<Evidencia> evidencias(Postulacion postulacion) {
        return postulacionEvidenciaRepository.findByPostulacion(postulacion)
                .stream()
                .map(PostulacionEvidencia::getEvidencia)
                .toList();
    }

    // ===== F1: filtros/paginación =====
    public record PostulacionFiltro(Long idOportunidad, Long idAlumno, List<EstadoPostulacion> estados,
                              String fechaDesde, String fechaHasta, String texto) {}

    @QueryMapping
    public com.vias.uc.backend.graphql.dto.PostulacionPageDTO postulacionesPage(
            @Argument PostulacionFiltro filtro,
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

    // ===== F2: query para empresa (ofertante) =====
    record FiltroPostulacionInput(
            Long idOportunidad,
            Long idAlumno,
            List<EstadoPostulacion> estados,
            String fechaDesde,
            String fechaHasta,
            String texto
    ) {}

    @QueryMapping
    public PostulacionPageDTO postulacionesEmpresa(@Argument Long idOfertante,
                                                   @Argument int page,
                                                   @Argument int size,
                                                   @Argument(name = "sort") String sort,
                                                   @Argument(name = "filtro") FiltroPostulacionInput filtro) {

        // --- Validación básica: que el usuario sea empresa ---
        Usuario usuario = usuarioRepository.findById(idOfertante)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + idOfertante));

        RolUsuario rol = usuario.getRolPrincipal();
        if (rol != RolUsuario.empresa) {
            throw new AccessDeniedException("Sólo perfiles de empresa pueden ver sus postulaciones.");
        }

        // --- Orden ---
        Sort orden = Sort.by("fechaPostulacion").descending(); // default
        if (sort != null && !sort.isBlank()) {
            String[] parts = sort.split(",");
            String prop = parts[0];
            boolean asc = parts.length < 2 || !"desc".equalsIgnoreCase(parts[1]);
            orden = asc ? Sort.by(prop).ascending() : Sort.by(prop).descending();
        }

        Pageable pageable = PageRequest.of(page, size, orden);

        // --- Filtros opcionales ---
        Long idOportunidad = filtro != null ? filtro.idOportunidad() : null;
        Long idAlumno = filtro != null ? filtro.idAlumno() : null;
        List<EstadoPostulacion> estados = filtro != null ? filtro.estados() : null;
        String desde = filtro != null ? filtro.fechaDesde() : null;
        String hasta = filtro != null ? filtro.fechaHasta() : null;
        String texto = filtro != null ? filtro.texto() : null;

        // --- Llamada al service nuevo ---
        Page<Postulacion> res = postulacionService.buscarConFiltrosPorOfertante(
                idOfertante,
                idOportunidad,
                idAlumno,
                estados,
                desde,
                hasta,
                texto,
                pageable
        );

        return new PostulacionPageDTO(
                res.getContent(),
                (int) res.getTotalElements(),
                page,
                size
        );
    }

    // ===== F1: cambio de estado =====
    @MutationMapping
    public Postulacion actualizarEstadoPostulacion(@Argument Long idPostulacion,
                                                   @Argument EstadoPostulacion estado,
                                                   @Argument String motivo,
                                                   @Argument Long idActor) {
        // 🔹 Validar permisos por rol
        Usuario actor = usuarioRepository.findById(idActor)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + idActor));
        assertPermisoActualizar(actor, estado);

        // continuar como ya tenías
        return postulacionService.actualizarEstado(idPostulacion, estado, motivo, idActor);
    }

    private void assertPermisoActualizar(Usuario actor, EstadoPostulacion nuevoEstado) {
        if (actor == null || actor.getRolPrincipal() == null) {
            throw new AccessDeniedException("No autorizado");
        }

        RolUsuario rol = actor.getRolPrincipal();

        if (rol == RolUsuario.alumno) {
            if (nuevoEstado != EstadoPostulacion.CANCELADA) {
                throw new AccessDeniedException("Un alumno solo puede cancelar su propia postulación");
            }
            return;
        }

        // Empresa / Profesor / Administrador: permitido (aceptar, rechazar, cancelar)
        if (rol == RolUsuario.investigador || rol == RolUsuario.profesor || rol == RolUsuario.administrador) {
            return;
        }

        // Otros roles (egresado, investigador si no corresponde): bloqueado
        throw new AccessDeniedException("No autorizado");
    }

    @QueryMapping
    public List<Evidencia> evidenciasPorAlumno(@Argument Long idAlumno) {
        return postulacionService.evidenciasPorAlumno(idAlumno);
    }

}
