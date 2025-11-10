package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.dto.EstadoPostulacionOutput;
import com.vias.uc.backend.service.PostulacionAplicacionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class PostulacionResolver {

    private final PostulacionAplicacionService service;

    public PostulacionResolver(PostulacionAplicacionService service) {
        this.service = service;
    }

    public EstadoPostulacionOutput estadoPostulacion(@Argument Integer idPostulacion,
                                                     @Argument Integer idPostulante) {
        return service.estado(idPostulacion, idPostulante);
    }
}
