package com.vias.uc.backend.graphql; // O donde tengas tus otros Resolvers

import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.service.BolsaTrabajoService; // El servicio que creamos antes
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BolsaTrabajoResolver {

    private final BolsaTrabajoService bolsaTrabajoService;

    public BolsaTrabajoResolver(BolsaTrabajoService bolsaTrabajoService) {
        this.bolsaTrabajoService = bolsaTrabajoService;
    }

    /**
     * ESTE ES EL RECORD QUE NO ENCONTRABAS.
     * Define el objeto de entrada para el filtro de GraphQL.
     * Los nombres (area, ubicacion, modalidad, empresa) deben coincidir
     * con tu schema de GraphQL.
     */
    public record BolsaTrabajoFiltro(
            String area,
            String ubicacion,
            String modalidad,
            String empresa // Acepta un String para el nombre (ej. "Tigo")
    ) {}


    /**
     * Este es el método que responde a tu query 'bolsaTrabajo'.
     * Toma el 'BolsaTrabajoFiltro' de la query y se lo pasa al servicio.
     */
    @QueryMapping
    public List<Oportunidad> bolsaTrabajo(@Argument BolsaTrabajoFiltro filtro) {

        // Extraemos los valores del filtro
        String area = (filtro != null) ? filtro.area() : null;
        String ubicacion = (filtro != null) ? filtro.ubicacion() : null;
        String modalidad = (filtro != null) ? filtro.modalidad() : null;
        String nombreEmpresa = (filtro != null) ? filtro.empresa() : null;

        // Llamamos al servicio que ya tiene la lógica de JOINs
        // que hicimos en el paso anterior.
        return bolsaTrabajoService.consultarOportunidades(
                ubicacion,
                modalidad,
                nombreEmpresa
        );
    }
}