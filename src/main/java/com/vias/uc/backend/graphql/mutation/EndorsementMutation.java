package com.vias.uc.backend.graphql.mutation;

import com.vias.uc.backend.model.Endorsement;
import com.vias.uc.backend.service.EndorsementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class EndorsementMutation {

    @Autowired
    private EndorsementService service;

    @MutationMapping
    public Endorsement crearEndorsement(@Argument Integer emisorId,
                                        @Argument Integer receptorId) {
        return service.crearEndorsement(emisorId, receptorId);
    }

    @MutationMapping
    public Boolean eliminarEndorsement(@Argument Integer id) {
        service.eliminarEndorsement(id);
        return true;
    }
}
