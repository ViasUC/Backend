package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Evidencia;
import com.vias.uc.backend.model.Portafolio;
import com.vias.uc.backend.repository.EvidenciaRepository;
import com.vias.uc.backend.repository.PortafolioRepository;

import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.Argument;

import java.util.Collections;
import java.util.List;

@Controller
public class PortafolioResolver {

    private final PortafolioRepository portafolioRepo;
    private final EvidenciaRepository evidenciaRepo;

    public PortafolioResolver(
            PortafolioRepository portafolioRepo,
            EvidenciaRepository evidenciaRepo
    ) {
        this.portafolioRepo = portafolioRepo;
        this.evidenciaRepo = evidenciaRepo;
    }

    // ✅ Obtener portafolio por usuario
    @QueryMapping
    public Portafolio portafolioPorUsuario(@Argument Long idUsuario) {
        return portafolioRepo.findByIdUsuario(idUsuario).orElse(null);
    }

    // ✅ Resolver de evidencias dentro del Portafolio
    @SchemaMapping(typeName = "Portafolio", field = "evidencias")
    public List<Evidencia> evidencias(Portafolio p) {
        List<Evidencia> lista = evidenciaRepo.findByIdPortafolio(Math.toIntExact(p.getIdPortafolio()));

        // ✅ No podés devolver null porque el schema dice [Evidencia!]!
        return lista != null ? lista : Collections.emptyList();
    }
}
