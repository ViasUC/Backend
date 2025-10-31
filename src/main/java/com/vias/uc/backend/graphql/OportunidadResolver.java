package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.repository.OportunidadRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OportunidadResolver {

    private final OportunidadRepository oportunidadRepository;

    public OportunidadResolver(OportunidadRepository oportunidadRepository) {
        this.oportunidadRepository = oportunidadRepository;
    }

    @QueryMapping
    public List<Oportunidad> oportunidades() {
        return oportunidadRepository.findAll();
    }

    @QueryMapping
    public Oportunidad oportunidad(@Argument Long id) {
        return oportunidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + id));
    }
}
