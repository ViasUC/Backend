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

    public PostulacionService(PostulacionRepository postulacionRepository,
                              AlumnoRepository alumnoRepository,
                              OportunidadRepository oportunidadRepository,
                              HistorialPostulacionRepository historialRepository,
                              UsuarioRepository usuarioRepository,
                              AuditoriaRepository auditoriaRepository) {
        this.postulacionRepository = postulacionRepository;
        this.alumnoRepository = alumnoRepository;
        this.oportunidadRepository = oportunidadRepository;
        this.historialRepository = historialRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional
    public Postulacion crearPostulacion(Long idAlumno, Long idOportunidad) {
        // Alumno: PK Long
        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + idAlumno));

        // Oportunidad: PK Integer
        Oportunidad oportunidad = oportunidadRepository.findById(Math.toIntExact(idOportunidad))
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + idOportunidad));

        Usuario postulante = Optional.ofNullable(alumno.getUsuario())
                .orElseThrow(() -> new RuntimeException("El Alumno " + idAlumno + " no tiene Usuario asociado."));

        // Resolver ofertante desde id_creador de la oportunidad (Integer -> Long para repo de Usuario)
        Integer idCreador = oportunidad.getIdCreador();
        if (idCreador == null) {
            throw new RuntimeException("La oportunidad " + idOportunidad + " no tiene id_creador (ofertante).");
        }
        Usuario ofertante = usuarioRepository.findById(idCreador.longValue())
                .orElseThrow(() -> new RuntimeException("Ofertante inexistente: " + idCreador));

        // Auditoría obligatoria
        Auditoria au = Auditoria.builder()
                .accion("CREAR_POSTULACION")
                .detalle("Alumno " + idAlumno + " postula a oportunidad " + idOportunidad)
                .actorId(postulante.getIdUsuario())
                .fechaEvento(LocalDateTime.now())
                .build();
        auditoriaRepository.save(au);

        Postulacion p = new Postulacion();
        p.setAlumno(alumno);
        p.setOportunidad(oportunidad);
        p.setPostulante(postulante);
        p.setOfertante(ofertante);
        p.setEstado(EstadoPostulacion.PENDIENTE);
        p.setFechaPostulacion(LocalDateTime.now());
        p.setAuditoria(au); // <- evita el NULL en id_auditoria

        Postulacion guardada = postulacionRepository.save(p);

        // primer historial
        HistorialPostulacion h = new HistorialPostulacion();
        h.setPostulacion(guardada);
        h.setEstadoAnterior(null);
        h.setEstadoNuevo(EstadoPostulacion.PENDIENTE);
        h.setMotivo("Creación de postulación");
        historialRepository.save(h);

        return guardada;
    }

    // ===== Listados simples =====
    @Transactional(readOnly = true)
    public List<Postulacion> listarTodas() {
        return postulacionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Postulacion> listarPorAlumno(Long idAlumno) {
        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + idAlumno));
        return postulacionRepository.findByAlumno(alumno);
    }

    @Transactional(readOnly = true)
    public List<Postulacion> listarPorOportunidad(Long idOportunidad) {
        Oportunidad oportunidad = oportunidadRepository.findById(Math.toIntExact(idOportunidad))
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + idOportunidad));
        return postulacionRepository.findByOportunidad(oportunidad);
    }

    // ===== Búsqueda con filtros =====
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

        Alumno alumno = null;
        if (idAlumno != null) {
            alumno = alumnoRepository.findById(idAlumno)
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + idAlumno));
        }

        LocalDateTime desde = parseFechaInicio(fechaDesdeStr);
        LocalDateTime hasta = parseFechaFin(fechaHastaStr);

        Specification<Postulacion> spec = Specification.where(PostulacionSpecifications.porOportunidad(op))
                .and(PostulacionSpecifications.porAlumno(alumno))
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

    // ===== Transiciones de estado + historial =====
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
}
