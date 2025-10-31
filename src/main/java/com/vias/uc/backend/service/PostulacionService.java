package com.vias.uc.backend.service;

import com.vias.uc.backend.model.*;
import com.vias.uc.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostulacionService {

    private final PostulacionRepository postulacionRepository;
    private final AlumnoRepository alumnoRepository;
    private final OportunidadRepository oportunidadRepository;

    public PostulacionService(PostulacionRepository postulacionRepository,
                              AlumnoRepository alumnoRepository,
                              OportunidadRepository oportunidadRepository) {
        this.postulacionRepository = postulacionRepository;
        this.alumnoRepository = alumnoRepository;
        this.oportunidadRepository = oportunidadRepository;
    }

    @Transactional
    public Postulacion crearPostulacion(Long idAlumno, Long idOportunidad) {
        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + idAlumno));

        Oportunidad oportunidad = oportunidadRepository.findById(idOportunidad)
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + idOportunidad));

        Usuario postulante = alumno.getUsuario();
        if (postulante == null) {
            throw new RuntimeException("El Alumno " + idAlumno + " no tiene Usuario asociado (postulante).");
        }

        Postulacion p = new Postulacion();
        p.setAlumno(alumno);
        p.setOportunidad(oportunidad);
        p.setPostulante(postulante); // 👈 clave para no romper NOT NULL
        p.setEstado(EstadoPostulacion.PENDIENTE);
        p.setFechaPostulacion(LocalDateTime.now());

        return postulacionRepository.save(p);
    }

    public List<Postulacion> listarTodas() {
        return postulacionRepository.findAll();
    }

    public List<Postulacion> listarPorAlumno(Long idAlumno) {
        Alumno alumno = alumnoRepository.findById(idAlumno)
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + idAlumno));
        return postulacionRepository.findByAlumno(alumno);
    }

    public List<Postulacion> listarPorOportunidad(Long idOportunidad) {
        Oportunidad oportunidad = oportunidadRepository.findById(idOportunidad)
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + idOportunidad));
        return postulacionRepository.findByOportunidad(oportunidad);
    }


}
