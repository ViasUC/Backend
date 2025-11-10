package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.dto.CrearPostulacionInput;
import com.vias.uc.backend.model.dto.PostulacionOutput;
import com.vias.uc.backend.model.dto.VincularEvidenciasInput;
import com.vias.uc.backend.service.PostulacionAplicacionService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PostulacionMutation {

    private final PostulacionAplicacionService service;

    public PostulacionMutation(PostulacionAplicacionService service) {
        this.service = service;
    }

    @MutationMapping
    public PostulacionOutput crearYEnviarPostulacion(@Argument CrearPostulacionInput input) {
        return service.crearYEnviar(input);
    }

    @MutationMapping
    public PostulacionOutput vincularEvidenciasPostulacion(@Argument VincularEvidenciasInput input,
                                                           @Argument Integer idPostulante) {
        return service.vincularEvidencias(input, idPostulante);
    }
}
