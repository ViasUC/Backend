package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Alumno;
import com.vias.uc.backend.repository.AlumnoRepository;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class AlumnoQuery {

    private final AlumnoRepository alumnoRepository;

    public AlumnoQuery(AlumnoRepository alumnoRepository) {
        this.alumnoRepository = alumnoRepository;
    }

    @QueryMapping
    public List<Alumno> alumnos() {
        return alumnoRepository.findAll();
    }
}
