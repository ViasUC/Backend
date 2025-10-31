package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.service.AlumnoService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class AlumnoResolver {

    private final AlumnoService alumnoService;

    public AlumnoResolver(AlumnoService alumnoService) {
        this.alumnoService = alumnoService;
    }

    // === Consultas ===
    @QueryMapping
    public List<Alumno> alumnos() {
        return alumnoService.listarAlumnos();
    }

    @QueryMapping
    public Optional<Alumno> alumnoPorId(@Argument Long id) {
        return alumnoService.obtenerAlumnoPorId(id);
    }

    // === Mutación ===
    @MutationMapping
    public Alumno crearAlumno(@Argument String nombre,
                              @Argument String apellido,
                              @Argument String email,
                              @Argument String carrera,
                              @Argument Integer semestre) {
        return alumnoService.crearAlumno(nombre, apellido, email, carrera, semestre);
    }
}
