package com.vias.uc.backend.graphql.query;

// --- IMPORTACIONES NECESARIAS ---
import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.model.dto.RegistroAlumnoInput;
import com.vias.uc.backend.repository.AlumnoRepository;
import com.vias.uc.backend.service.AlumnoService; // 1. Importar el SERVICIO

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping; // 3. Importar la anotación de Mutación
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class AlumnoResolver {

    private final AlumnoService alumnoService;
    private final AlumnoRepository alumnoRepository;

    public AlumnoResolver(AlumnoService alumnoService, AlumnoRepository alumnoRepository) {
        this.alumnoService = alumnoService;
        this.alumnoRepository = alumnoRepository;
    }

    @MutationMapping
    public Alumno registrarAlumno(@Argument RegistroAlumnoInput input) {
        return alumnoService.registrarAlumnoMutationFede(input);
    }

    @QueryMapping
    public Alumno alumno(@Argument Integer id) {
        return alumnoService.getAlumno(id);
    }

    @QueryMapping
    public List<Alumno> alumnos() {
        return alumnoRepository.findAll();
    }

    @MutationMapping
    public Alumno actualizarAlumno(@Argument Integer id, @Argument com.vias.uc.backend.model.dto.AlumnoInput input) {
        System.out.println("=== [DEBUG] Entrando a Mutation actualizarAlumno ===");
        return alumnoService.actualizarAlumno(id, input);
    }

}
