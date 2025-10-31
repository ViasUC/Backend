package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Postulacion;
import com.vias.uc.backend.model.HistorialPostulacion;
import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.PostulacionRepository;
import com.vias.uc.backend.repository.HistorialPostulacionRepository;
import com.vias.uc.backend.repository.OportunidadRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostulacionService {

    private final PostulacionRepository postulacionRepository;
    private final HistorialPostulacionRepository historialRepo;
    private final OportunidadRepository oportunidadRepository;
    private final UsuarioRepository usuarioRepository;

    public PostulacionService(PostulacionRepository postulacionRepository,
                              HistorialPostulacionRepository historialRepo,
                              OportunidadRepository oportunidadRepository,
                              UsuarioRepository usuarioRepository) {
        this.postulacionRepository = postulacionRepository;
        this.historialRepo = historialRepo;
        this.oportunidadRepository = oportunidadRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Postulacion crear(Long usuarioId, Long oportunidadId, String mensaje, String curriculumUrl) {
        // Cargar asociaciones reales (no IDs sueltos)
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Oportunidad oportunidad = oportunidadRepository.findById(oportunidadId)
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada"));

        Postulacion postulacion = new Postulacion();
        postulacion.setUsuario(usuario);
        postulacion.setOportunidad(oportunidad);
        postulacion.setMensaje(mensaje);
        postulacion.setCurriculumUrl(curriculumUrl);
        postulacion.setEstado(Postulacion.EstadoPostulacion.PENDIENTE);

        postulacion = postulacionRepository.save(postulacion);

        // Traza inicial en historial
        HistorialPostulacion historial = new HistorialPostulacion();
        historial.setPostulacion(postulacion);
        historial.setEstadoAnterior(null);
        historial.setEstadoNuevo(Postulacion.EstadoPostulacion.PENDIENTE);
        historial.setMotivo("Creación de postulación");
        historialRepo.save(historial);

        return postulacion;
    }

    @Transactional
    public Postulacion actualizarEstado(Long idPostulacion, Postulacion.EstadoPostulacion nuevoEstado, String motivo) {
        Postulacion postulacion = postulacionRepository.findById(idPostulacion)
                .orElseThrow(() -> new RuntimeException("Postulación no encontrada"));

        Postulacion.EstadoPostulacion anterior = postulacion.getEstado();
        if (anterior == nuevoEstado) {
            return postulacion;
        }

        postulacion.setEstado(nuevoEstado);
        postulacion = postulacionRepository.save(postulacion);

        // Registrar historial
        HistorialPostulacion historial = new HistorialPostulacion();
        historial.setPostulacion(postulacion);
        historial.setEstadoAnterior(anterior);
        historial.setEstadoNuevo(nuevoEstado);
        historial.setMotivo(motivo);
        historialRepo.save(historial);

        return postulacion;
    }

    @Transactional
    public Page<Postulacion> listarPorOportunidad(Long oportunidadId,
                                                  Postulacion.EstadoPostulacion estado,
                                                  String q,
                                                  Pageable pageable) {
        return postulacionRepository.buscarPorOportunidad(oportunidadId, estado, q, pageable);
    }
}
