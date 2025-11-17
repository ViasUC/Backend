package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Endorsement;
import com.vias.uc.backend.model.Endorsement.Status;
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

    // DTO de entrada (record)
    public record CreateEndorsementInput(Integer fromUserId, Integer toUserId, String skill, String message) {}
    public record DecideEndorsementInput(Long id, Integer actorId, Boolean accept) {}

    // ---- Mutations ----
    @MutationMapping
    public Endorsement createEndorsement(@Argument CreateEndorsementInput input) {
        return service.create(input.fromUserId(), input.toUserId(), input.skill(), input.message());
    }

    @MutationMapping
    public Endorsement decideEndorsement(@Argument DecideEndorsementInput input) {
        return service.decide(
                input.id(),
                input.actorId(),   // <--- reemplaza auth.getUserId()
                input.accept()
        );
    }

    // ---- Queries ----
    @QueryMapping
    public List<Endorsement> endorsementsReceived(
            @Argument Integer toUserId,
            @Argument Status status
    ) {
        return service.inbox(toUserId, status);
    }

    @QueryMapping
    public List<Endorsement> endorsementsGiven(@Argument Integer fromUserId) {
        return service.given(fromUserId);
    }

    @QueryMapping
    public List<Endorsement> publicEndorsementsOf(@Argument Integer userId) {
        return service.publicForUser(userId);
    }
}
