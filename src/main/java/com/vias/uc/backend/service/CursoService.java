package com.vias.uc.backend.service;

import com.vias.uc.backend.model.Curso;
import com.vias.uc.backend.repository.CursoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    /**
     * Lista cursos con lógica de filtrado:
     * - area y modalidad -> filtra por ambos
     * - solo area -> filtra por area
     * - solo modalidad -> filtra por modalidad
     * - ninguno -> devuelve todos
     */
    public List<Curso> listarCursos(String area, String modalidad) {

        boolean tieneArea = area != null && !area.isBlank();
        boolean tieneModalidad = modalidad != null && !modalidad.isBlank();

        if (tieneArea && tieneModalidad) {
            return cursoRepository.findByAreaAndModalidad(area, modalidad);
        }

        if (tieneArea) {
            return cursoRepository.findByArea(area);
        }

        if (tieneModalidad) {
            return cursoRepository.findByModalidad(modalidad);
        }

        // Sin filtros: todos los cursos
        return cursoRepository.findAll();
    }

    /**
     * Filtro explícito por área (si no viene área, devuelve todos)
     */
    public List<Curso> filtrarPorArea(String area) {
        if (area == null || area.isBlank()) {
            return cursoRepository.findAll();
        }
        return cursoRepository.findByArea(area);
    }

    /**
     * Filtro explícito por modalidad (si no viene modalidad, devuelve todos)
     */
    public List<Curso> filtrarPorModalidad(String modalidad) {
        if (modalidad == null || modalidad.isBlank()) {
            return cursoRepository.findAll();
        }
        return cursoRepository.findByModalidad(modalidad);
    }

    /**
     * Por si querés algo directo sin filtros desde otros lugares
     */
    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }
}
