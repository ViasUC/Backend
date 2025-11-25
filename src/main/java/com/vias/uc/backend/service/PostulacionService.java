package com.vias.uc.backend.service;

import com.vias.uc.backend.model.*;
import com.vias.uc.backend.repository.*;
import com.vias.uc.backend.repository.spec.PostulacionSpecifications;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class PostulacionService {

    private final PostulacionRepository postulacionRepository;
    private final AlumnoRepository alumnoRepository;
    private final OportunidadRepository oportunidadRepository;
    private final HistorialPostulacionRepository historialRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaRepository auditoriaRepository;

    // >>> NUEVO: repositorios para evidencias y la tabla intermedia
    private final EvidenciaRepository evidenciaRepository;
    private final PostulacionEvidenciaRepository postulacionEvidenciaRepository;
    private final PortafolioRepository portafolioRepository;

    public PostulacionService(PostulacionRepository postulacionRepository,
                              AlumnoRepository alumnoRepository,
                              OportunidadRepository oportunidadRepository,
                              HistorialPostulacionRepository historialRepository,
                              UsuarioRepository usuarioRepository,
                              AuditoriaRepository auditoriaRepository,
                              // >>> NUEVO: inyectar estos dos
                              EvidenciaRepository evidenciaRepository,
                              PostulacionEvidenciaRepository postulacionEvidenciaRepository,
                              PortafolioRepository portafolioRepository) {
        this.postulacionRepository = postulacionRepository;
        this.alumnoRepository = alumnoRepository;
        this.oportunidadRepository = oportunidadRepository;
        this.historialRepository = historialRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.evidenciaRepository = evidenciaRepository;                         // >>> NUEVO
        this.postulacionEvidenciaRepository = postulacionEvidenciaRepository;
        this.portafolioRepository = portafolioRepository; // >>> NUEVO
    }

    // >>> NUEVO: método “clásico” delega al nuevo que acepta evidencias
    @Transactional
    public Postulacion crearPostulacion(Long idAlumno, Long idOportunidad) {
        // mantiene la firma original usada por el resolver
        return crearPostulacion(idAlumno, idOportunidad, null);
    }

    // >>> NUEVO: sobrecarga que permite vincular evidencias opcionales
    @Transactional
    public Postulacion crearPostulacion(Long idAlumno,
                                        Long idOportunidad,
                                        List<Integer> idsEvidencias) {

        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + idAlumno));

        Oportunidad oportunidad = oportunidadRepository.findById(Math.toIntExact(idOportunidad))
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + idOportunidad));

        Usuario postulante = Optional.ofNullable(alumno.getUsuario())
                .orElseThrow(() -> new RuntimeException("El Alumno " + idAlumno + " no tiene Usuario asociado."));

        Integer idCreador = oportunidad.getIdCreador();
        if (idCreador == null) {
            throw new RuntimeException("La oportunidad " + idOportunidad + " no tiene id_creador (ofertante).");
        }
        Usuario ofertante = usuarioRepository.findById(Long.valueOf(idCreador))
                .orElseThrow(() -> new RuntimeException("Ofertante inexistente: " + idCreador));

        Auditoria au = Auditoria.builder()
                .accion("CREAR_POSTULACION")
                .detalle("Alumno " + idAlumno + " postula a oportunidad " + idOportunidad)
                .actorId(Math.toIntExact(postulante.getIdUsuario()))
                .fechaEvento(LocalDateTime.now())
                .build();
        auditoriaRepository.save(au);

        Postulacion p = new Postulacion();
        p.setOportunidad(oportunidad);
        p.setPostulante(postulante);
        p.setOfertante(ofertante);
        p.setEstado(EstadoPostulacion.PENDIENTE);
        p.setFechaPostulacion(LocalDateTime.now());
        p.setAuditoria(au);

        Postulacion guardada = postulacionRepository.save(p);

        HistorialPostulacion h = new HistorialPostulacion();
        h.setPostulacion(guardada);
        h.setEstadoAnterior(null);
        h.setEstadoNuevo(EstadoPostulacion.PENDIENTE);
        h.setMotivo("Creación de postulación");
        historialRepository.save(h);

        // >>> NUEVO: vincular evidencias del portafolio si se enviaron
        vincularEvidenciasConPostulacion(guardada, alumno, idsEvidencias);

        return guardada;
    }

    // >>> NUEVO: helper para vincular evidencias opcionales
    private void vincularEvidenciasConPostulacion(Postulacion postulacion,
                                                  Alumno alumno,
                                                  List<Integer> idsEvidencias) {
        if (idsEvidencias == null || idsEvidencias.isEmpty()) {
            // no hay evidencias a asociar: requisito de “vinculación opcional” cumplido
            return;
        }

        Usuario postulante = alumno.getUsuario();
        if (postulante == null) {
            throw new RuntimeException("El alumno no tiene usuario asociado");
        }
        int idUsuarioPostulante = Math.toIntExact(postulante.getIdUsuario());

        // buscar todas las evidencias enviadas
        List<Evidencia> evidencias = evidenciaRepository.findAllById(idsEvidencias);

        if (evidencias.size() != idsEvidencias.size()) {
            throw new RuntimeException("Alguna evidencia no existe");
        }

        // validar que todas las evidencias pertenezcan al mismo usuario
        for (Evidencia evidencia : evidencias) {
            Integer idPortafolio = evidencia.getIdPortafolio();
            if (idPortafolio == null) {
                throw new RuntimeException("La evidencia " + evidencia.getIdEvidencia() + " no tiene id_portafolio asociado");
            }

            Portafolio portafolio = portafolioRepository.findById((int) idPortafolio.longValue())
                    .orElseThrow(() -> new RuntimeException(
                            "Portafolio no encontrado para la evidencia " + evidencia.getIdEvidencia()
                    ));

            // si tu Portafolio no tiene getIdUsuario() y sí getUsuario(), adaptá esta línea
            Integer idUsuarioDueno = Math.toIntExact(portafolio.getIdUsuario());
            if (!Objects.equals(idUsuarioDueno, idUsuarioPostulante)) {
                throw new RuntimeException(
                        "La evidencia " + evidencia.getIdEvidencia() + " no pertenece al alumno que se está postulando"
                );
            }
        }

        // auditoría específica para la vinculación de evidencias
        Auditoria auVinculo = Auditoria.builder()
                .accion("VINCULAR_EVIDENCIAS_POSTULACION")
                .detalle("Vincular evidencias a postulación " + postulacion.getIdPostulacion())
                .actorId(idUsuarioPostulante)
                .fechaEvento(LocalDateTime.now())
                .build();
        auditoriaRepository.save(auVinculo);

        // crear registros en postulacion_evidencia
        for (Evidencia evidencia : evidencias) {
            PostulacionEvidencia pe = new PostulacionEvidencia();
            pe.setPostulacion(postulacion);
            pe.setEvidencia(evidencia);
            pe.setAuditoria(auVinculo);

            postulacionEvidenciaRepository.save(pe);
        }
    }

    // ===================== RESTO DEL CÓDIGO ORIGINAL =====================

    @Transactional(readOnly = true)
    public List<Postulacion> listarTodas() {
        return postulacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Postulacion> listarPorAlumno(Long idAlumno) {
        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + idAlumno));
        Usuario usuario = Optional.ofNullable(alumno.getUsuario())
                .orElseThrow(() -> new RuntimeException("El Alumno " + idAlumno + " no tiene Usuario asociado."));
        return postulacionRepository.findByPostulante(usuario);
    }

    @Transactional(readOnly = true)
    public List<Postulacion> listarPorOportunidad(Long idOportunidad) {
        Oportunidad oportunidad = oportunidadRepository.findById(Math.toIntExact(idOportunidad))
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + idOportunidad));
        return postulacionRepository.findByOportunidad(oportunidad);
    }

    @Transactional(readOnly = true)
    public Page<Postulacion> buscarConFiltros(Long idOportunidad, Long idAlumno,
                                              List<EstadoPostulacion> estados,
                                              String fechaDesdeStr, String fechaHastaStr,
                                              String texto, Pageable pageable) {

        Oportunidad op = null;
        if (idOportunidad != null) {
            op = oportunidadRepository.findById(Math.toIntExact(idOportunidad))
                    .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + idOportunidad));
        }

        Usuario postulante = null;
        if (idAlumno != null) {
            Alumno alumno = alumnoRepository.findById(idAlumno)
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + idAlumno));
            postulante = Optional.ofNullable(alumno.getUsuario())
                    .orElseThrow(() -> new RuntimeException("El Alumno " + idAlumno + " no tiene Usuario asociado."));
        }

        LocalDateTime desde = parseFechaInicio(fechaDesdeStr);
        LocalDateTime hasta = parseFechaFin(fechaHastaStr);

        Specification<Postulacion> spec = Specification.allOf(PostulacionSpecifications.porOportunidad(op))
                .and(PostulacionSpecifications.porPostulante(postulante))
                .and(PostulacionSpecifications.porEstados(estados))
                .and(PostulacionSpecifications.desde(desde))
                .and(PostulacionSpecifications.hasta(hasta))
                .and(PostulacionSpecifications.texto(texto));

        return postulacionRepository.findAll(spec, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Postulacion> buscarConFiltrosPorOfertante(Long idOfertante,
                                                          Long idOportunidad,
                                                          Long idAlumno,
                                                          List<EstadoPostulacion> estados,
                                                          String fechaDesdeStr,
                                                          String fechaHastaStr,
                                                          String texto,
                                                          Pageable pageable) {

        // --- Oportunidad (opcional) ---
        Oportunidad op = null;
        if (idOportunidad != null) {
            op = oportunidadRepository.findById(Math.toIntExact(idOportunidad))
                    .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + idOportunidad));
        }

        // --- Postulante (opcional, por si querés filtrar también por alumno) ---
        Usuario postulante = null;
        if (idAlumno != null) {
            Alumno alumno = alumnoRepository.findById(idAlumno)
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + idAlumno));
            postulante = Optional.ofNullable(alumno.getUsuario())
                    .orElseThrow(() -> new RuntimeException("El Alumno " + idAlumno + " no tiene Usuario asociado."));
        }

        // --- Ofertante (empresa logueada) ---
        Usuario ofertante = null;
        if (idOfertante != null) {
            ofertante = usuarioRepository.findById(idOfertante)
                    .orElseThrow(() -> new RuntimeException("Ofertante no encontrado: " + idOfertante));
        }

        // --- Fechas ---
        LocalDateTime desde = parseFechaInicio(fechaDesdeStr);
        LocalDateTime hasta = parseFechaFin(fechaHastaStr);

        // --- Especificación con todos los filtros, incluido ofertante ---
        Specification<Postulacion> spec = Specification
                .allOf(PostulacionSpecifications.porOportunidad(op))
                .and(PostulacionSpecifications.porPostulante(postulante))
                .and(PostulacionSpecifications.porOfertante(ofertante))
                .and(PostulacionSpecifications.porEstados(estados))
                .and(PostulacionSpecifications.desde(desde))
                .and(PostulacionSpecifications.hasta(hasta))
                .and(PostulacionSpecifications.texto(texto));

        return postulacionRepository.findAll(spec, pageable);
    }

    private LocalDateTime parseFechaInicio(String s) {
        if (s == null || s.isBlank()) return null;
        try { return LocalDate.parse(s).atStartOfDay(); } catch (DateTimeParseException e) { return null; }
    }

    private LocalDateTime parseFechaFin(String s) {
        if (s == null || s.isBlank()) return null;
        try { return LocalDate.parse(s).atTime(23,59,59); } catch (DateTimeParseException e) { return null; }
    }

    private static final Map<EstadoPostulacion, Set<EstadoPostulacion>> TRANSICIONES = Map.of(
            EstadoPostulacion.PENDIENTE, Set.of(EstadoPostulacion.ACEPTADA, EstadoPostulacion.RECHAZADA, EstadoPostulacion.CANCELADA),
            EstadoPostulacion.ACEPTADA, Set.of(EstadoPostulacion.CANCELADA),
            EstadoPostulacion.RECHAZADA, Set.of(EstadoPostulacion.CANCELADA),
            EstadoPostulacion.CANCELADA, Set.of()
    );

    @Transactional
    public Postulacion actualizarEstado(Long idPostulacion, EstadoPostulacion nuevo, String motivo, Long idActor) {
        Postulacion p = postulacionRepository.findById(Math.toIntExact(idPostulacion))
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada: " + idPostulacion));

        EstadoPostulacion anterior = p.getEstado();
        if (anterior == nuevo) throw new RuntimeException("La postulación ya está en estado " + nuevo);

        Set<EstadoPostulacion> permitidos = TRANSICIONES.getOrDefault(anterior, Set.of());
        if (!permitidos.contains(nuevo)) {
            throw new RuntimeException("Transición no permitida: " + anterior + " -> " + nuevo);
        }

        p.setEstado(nuevo);
        postulacionRepository.save(p);

        HistorialPostulacion h = new HistorialPostulacion();
        h.setPostulacion(p);
        h.setEstadoAnterior(anterior);
        h.setEstadoNuevo(nuevo);
        h.setMotivo(motivo);

        if (idActor != null) {
            usuarioRepository.findById(idActor).ifPresent(h::setActor);
        }
        historialRepository.save(h);

        return p;
    }

    @Transactional(readOnly = true)
    public List<HistorialPostulacion> historial(Long idPostulacion) {
        Postulacion p = postulacionRepository.findById(Math.toIntExact(idPostulacion))
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada: " + idPostulacion));
        return historialRepository.findByPostulacionOrderByFechaCambioDesc(p);
    }

    public List<Evidencia> evidenciasPorAlumno(Long idAlumno) {

        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + idAlumno));

        Usuario usuario = Optional.ofNullable(alumno.getUsuario())
                .orElseThrow(() -> new RuntimeException("El alumno no tiene un usuario asociado"));

        Integer idUsuario = Math.toIntExact(usuario.getIdUsuario());

        // 1. El portafolio del usuario (solo uno!)
        Portafolio portafolio = portafolioRepository.findByIdUsuario(Long.valueOf(idUsuario))
                .orElse(null);

        if (portafolio == null) {
            return List.of(); // no tiene portafolio
        }

        Integer idPortafolio = portafolio.getIdPortafolio();

        // 2. Evidencias del único portafolio
        return evidenciaRepository.findByIdPortafolio(idPortafolio);
    }

}
