package com.vias.uc.backend.graphql.mutation;

import com.vias.uc.backend.model.Egresado;
import com.vias.uc.backend.model.dto.EgresadoInput;
import com.vias.uc.backend.service.EgresadoService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class EgresadoMutationResolver {

    private final EgresadoService egresadoService;

    public EgresadoMutationResolver(EgresadoService egresadoService) {
        this.egresadoService = egresadoService;
    }

    @MutationMapping
    public Egresado actualizarEgresado(
            @Argument Integer id,
            @Argument EgresadoInput input
    ) {
        return egresadoService.actualizarEgresado(id, input);
    }
}
