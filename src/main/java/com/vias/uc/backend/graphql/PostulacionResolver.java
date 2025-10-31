package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Postulacion;
import com.vias.uc.backend.service.PostulacionService;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class PostulacionResolver {

    private final PostulacionService postulacionService;

    public PostulacionResolver(PostulacionService postulacionService) {
        this.postulacionService = postulacionService;
    }

    @QueryMapping
    public List<Postulacion> postulaciones() {
        return postulacionService.listarTodas();
    }

    @QueryMapping
    public List<Postulacion> postulacionesPorAlumno(@Argument Long idAlumno) {
        return postulacionService.listarPorAlumno(idAlumno);
    }

    @QueryMapping
    public List<Postulacion> postulacionesPorOportunidad(@Argument Long idOportunidad) {
        return postulacionService.listarPorOportunidad(idOportunidad);
    }

    @MutationMapping
    public Postulacion crearPostulacion(@Argument Long idAlumno, @Argument Long idOportunidad) {
        return postulacionService.crearPostulacion(idAlumno, idOportunidad);
    }
}
