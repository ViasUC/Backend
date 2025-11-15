package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Evidencia;
import com.vias.uc.backend.repository.EvidenciaRepository;
import com.vias.uc.backend.repository.PortafolioRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EvidenciaQuery {

    private final EvidenciaRepository evRepo;
    private final PortafolioRepository portRepo;

    public EvidenciaQuery(EvidenciaRepository evRepo, PortafolioRepository portRepo) {
        this.evRepo = evRepo;
        this.portRepo = portRepo;
    }

    @QueryMapping
    public List<Evidencia> evidenciasPorPortafolio(@Argument Integer idPortafolio) {
        return evRepo.findByIdPortafolio(idPortafolio);
    }

    // ✅ Alternativa: traer evidencias directamente por usuario (usa su portafolio)
    @QueryMapping
    public List<Evidencia> evidenciasPorUsuario(@Argument Integer idUsuario) {
        var port = portRepo.findByIdUsuario(Long.valueOf(idUsuario))
                .orElseThrow(() -> new IllegalArgumentException("El usuario no tiene portafolio"));
        return evRepo.findByIdPortafolio(port.getIdPortafolio());
    }

}
