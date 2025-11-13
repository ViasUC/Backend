package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Egresado;
import com.vias.uc.backend.model.dto.EgresadoInput;
import com.vias.uc.backend.repository.EgresadoRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class EgresadoMutationResolver {

    private final EgresadoRepository egresadoRepo;

    public EgresadoMutationResolver(EgresadoRepository egresadoRepo) {
        this.egresadoRepo = egresadoRepo;
    }

    @MutationMapping
    public Egresado actualizarEgresado(
            @Argument Integer id,
            @Argument EgresadoInput input
    ) {

        return egresadoRepo.findById(id).map(e -> {

            if (input.titulo() != null)
                e.setTitulo(input.titulo());   // <-- AHORA SÍ EXISTE

            if (input.anioEgreso() != null)
                e.setAnioEgreso(input.anioEgreso());

            return egresadoRepo.save(e);

        }).orElse(null);
    }
}
