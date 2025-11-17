package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Publicacion;
import com.vias.uc.backend.model.canales.CanalInformacion;
import com.vias.uc.backend.model.enums.TipoCanal;
import com.vias.uc.backend.service.CanalService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CanalController {

    private final CanalService service;

    public CanalController(CanalService service) {
        this.service = service;
    }

    // ====== Inputs que usa GraphQL (coinciden con el schema) ======

    public record CrearCanalInput(
            String nombre,
            String slug,
            TipoCanal tipo,
            String descripcion,
            Long actorId
    ) {}

    public record CrearPublicacionEnCanalInput(
            Integer idCanal,
            Integer idProyectoF7,
            String titulo,
            Integer idAutor,
            String contenido
    ) {}

    // ================= QUERIES =================

    @QueryMapping
    public List<CanalInformacion> canalesActivos() {
        return service.canalesActivos();
    }

    @QueryMapping
    public List<Publicacion> publicacionesDeCanal(@Argument Integer idCanal) {
        return service.publicacionesDeCanal(idCanal);
    }

    // ================= MUTATIONS =================

    @MutationMapping
    public CanalInformacion crearCanal(@Argument CrearCanalInput input) {
        return service.crearCanal(
                input.nombre(),
                input.slug(),
                input.tipo(),
                input.descripcion(),
                input.actorId()
        );
    }

    @MutationMapping
    public Publicacion crearPublicacionEnCanal(@Argument CrearPublicacionEnCanalInput input) {
        return service.crearPublicacionEnCanal(
                input.idCanal(),
                input.idProyectoF7(),
                input.idAutor(),
                input.titulo(),
                input.contenido()
        );
    }

    @MutationMapping
    public Boolean seguirCanal(@Argument Integer idCanal, @Argument Integer idUsuario) {
        return service.seguirCanal(idCanal, idUsuario);
    }

    @MutationMapping
    public Boolean dejarDeSeguirCanal(@Argument Integer idCanal, @Argument Integer idUsuario) {
        return service.dejarDeSeguirCanal(idCanal, idUsuario);
    }

    @QueryMapping
    public List<CanalInformacion> canalesSeguidos(@Argument Integer idUsuario) {
        return service.canalesSeguidosPorUsuario(idUsuario);
    }

    @QueryMapping
    public List<Publicacion> feedCanalesSeguidos(@Argument Integer idUsuario) {
        return service.feedCanalesSeguidos(idUsuario);
    }

    @MutationMapping
    public Boolean destacarPublicacion(@Argument Integer idCanal,
                                       @Argument Integer idPublicacion,
                                       @Argument Boolean destacado) {
        return service.destacarPublicacion(idCanal, idPublicacion, destacado);
    }

    @QueryMapping
    public List<CanalInformacion> canalesPorTipo(@Argument TipoCanal tipo) {
        return service.obtenerCanalesPorTipo(tipo);
    }
}
