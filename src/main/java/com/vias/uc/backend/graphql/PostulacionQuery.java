package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.dto.EstadoPostulacionOutput;
import com.vias.uc.backend.service.PostulacionAplicacionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PostulacionQuery {

    private final PostulacionAplicacionService service;

    public PostulacionQuery(PostulacionAplicacionService service) {
        this.service = service;
    }

    @QueryMapping
    public EstadoPostulacionOutput estadoPostulacion(@Argument Integer idPostulacion,
                                                     @Argument Integer idPostulante) {
        return service.estado(idPostulacion, idPostulante);
    }
}
