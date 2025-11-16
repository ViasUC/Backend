package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Postulacion;
import com.vias.uc.backend.repository.PostulacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostulacionQuery {

    private final PostulacionRepository repo;

    @QueryMapping
    public List<Postulacion> postulacionesPorUsuario(@Argument Integer idUsuario) {
        return repo.findByIdPostulante(idUsuario);
    }
}
