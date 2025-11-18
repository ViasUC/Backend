package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Endorsement;
import com.vias.uc.backend.service.EndorsementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EndorsementQuery {

    @Autowired
    private EndorsementService service;

    @QueryMapping
    public List<Endorsement> endorsementsRealizados(@Argument Integer id) {
        return service.obtenerRealizados(id);
    }

    @QueryMapping
    public List<Endorsement> endorsementsRecibidos(@Argument Integer id) {
        return service.obtenerRecibidos(id);
    }
}
