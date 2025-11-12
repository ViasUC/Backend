package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Endorsement;
import com.vias.uc.backend.model.Endorsement.Status;
import com.vias.uc.backend.service.AuthDocente;
import com.vias.uc.backend.service.AuthService;
import com.vias.uc.backend.service.EndorsementService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EndorsementController {

    private final EndorsementService service;
    private final AuthDocente auth; // reemplaza por tu clase real de auth (de donde sacás el userId)

    // DTO de entrada (record)
    public record CreateEndorsementInput(Integer toUserId, String skill, String message) {}

    // ---- Mutations ----
    @MutationMapping
    public Endorsement createEndorsement(@Argument CreateEndorsementInput input) {
        return service.create(auth.getUserId(), input.toUserId(), input.skill(), input.message());
    }

    @MutationMapping
    public Endorsement decideEndorsement(@Argument Long id, @Argument Boolean accept) {
        return service.decide(id, auth.getUserId(), accept);
    }

    // ---- Queries ----
    @QueryMapping
    public List<Endorsement> endorsementsReceived(@Argument Status status) {
        return service.inbox(auth.getUserId(), status);
    }

    @QueryMapping
    public List<Endorsement> endorsementsGiven() {
        return service.given(auth.getUserId());
    }

    @QueryMapping
    public List<Endorsement> publicEndorsementsOf(@Argument Integer userId) {
        return service.publicForUser(userId);
    }
}
