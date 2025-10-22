package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.AlumnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.*;
import lombok.Data;

@Service
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    @Autowired
    public AlumnoService(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    public List<Alumno> obtenerTodos() {
        return alumnoRepository.findAll();
    }

    public Optional<Alumno> obtenerPorId(Integer id) {
        return alumnoRepository.findById(id);
    }

    public Alumno crearAlumno(Alumno alumno) {
        return alumnoRepository.save(alumno);
    }

    public Alumno actualizarAlumno(Integer id, Alumno nuevosDatos) {
        return alumnoRepository.findById(id)
                .map(alumno -> {
                    // Actualiza datos del usuario (nombre, apellido, email, etc.)
                    Usuario usuario = alumno.getUsuario();
                    if (usuario != null && nuevosDatos.getUsuario() != null) {
                        usuario.setNombre(nuevosDatos.getUsuario().getNombre());
                        usuario.setApellido(nuevosDatos.getUsuario().getApellido());
                        usuario.setEmail(nuevosDatos.getUsuario().getEmail());
                        usuario.setTelefono(nuevosDatos.getUsuario().getTelefono());
                        usuario.setUbicacion(nuevosDatos.getUsuario().getUbicacion());
                    }

                    // Actualiza datos del alumno
                    alumno.setCarrera(nuevosDatos.getCarrera());
                    alumno.setSemestre(nuevosDatos.getSemestre());
                    alumno.setIdAuditoria(nuevosDatos.getIdAuditoria());

                    return alumnoRepository.save(alumno);
                })
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con id: " + id));
    }



    public void eliminarAlumno(Integer id) {
        alumnoRepository.deleteById(id);
    }
}
