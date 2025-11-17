package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Curso;
import com.vias.uc.backend.service.CursoService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class CursoResolver {

    private final CursoService cursoService;

    public CursoResolver(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @QueryMapping
    public List<Curso> cursos(@Argument String area,
                              @Argument String modalidad) {
        return cursoService.listarCursos(area, modalidad);
    }
}
