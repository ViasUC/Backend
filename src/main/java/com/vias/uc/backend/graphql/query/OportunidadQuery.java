package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.repository.OportunidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OportunidadQuery {

    private final OportunidadRepository repo;

    @QueryMapping
    public List<Oportunidad> oportunidades() {
        return repo.findAll();
    }

    @QueryMapping
    public Oportunidad oportunidadById(@Argument Integer id) {
        return repo.findById(id).orElse(null);
    }
}
