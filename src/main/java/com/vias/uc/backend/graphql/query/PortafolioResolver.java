package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Evidencia;
import com.vias.uc.backend.model.Portafolio;
import com.vias.uc.backend.repository.EvidenciaRepository;
import com.vias.uc.backend.repository.PortafolioRepository;
import com.vias.uc.backend.service.PortafolioService;

import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.method.annotation.Argument;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PortafolioResolver {

    private final PortafolioRepository portafolioRepo;
    private final EvidenciaRepository evidenciaRepo;
    private final PortafolioService portafolioService;

    public PortafolioResolver(
            PortafolioRepository portafolioRepo,
            EvidenciaRepository evidenciaRepo,
            PortafolioService portafolioService
    ) {
        this.portafolioRepo = portafolioRepo;
        this.evidenciaRepo = evidenciaRepo;
        this.portafolioService = portafolioService;
    }

    @QueryMapping
    public Portafolio portafolioPorUsuario(@Argument Long idUsuario) {
        return portafolioRepo.findByIdUsuario(idUsuario).orElse(null);
    }

    @SchemaMapping(typeName = "Portafolio", field = "evidencias")
    public List<Evidencia> evidencias(Portafolio p) {
        List<Evidencia> lista = evidenciaRepo.findByIdPortafolio(Math.toIntExact(p.getIdPortafolio()));
        return lista != null ? lista : Collections.emptyList();
    }
    
    /**
     * Buscar portafolios con filtros opcionales
     */
    @QueryMapping
    public Map<String, Object> buscarPortafolios(@Argument Map<String, Object> filtros) {
        System.out.println(">>> Query buscarPortafolios recibido");
        System.out.println(">>> Filtros: " + filtros);
        
        if (filtros == null) {
            filtros = new HashMap<>();
        }
        
        return portafolioService.buscarPortafolios(filtros);
    }
    
    /**
     * Obtener filtros disponibles (carreras, ubicaciones)
     */
    @QueryMapping
    public Map<String, Object> obtenerFiltros() {
        System.out.println(">>> Query obtenerFiltros recibido");
        return portafolioService.obtenerFiltros();
    }
}
