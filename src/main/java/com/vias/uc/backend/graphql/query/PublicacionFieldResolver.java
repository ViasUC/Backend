package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.canales.CanalInformacion;
import com.vias.uc.backend.model.canales.CanalPublicacion;
import com.vias.uc.backend.repository.canales.CanalPublicacionRepository;
import com.vias.uc.backend.model.Publicacion;

import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PublicacionFieldResolver {

    private final CanalPublicacionRepository canalPublicacionRepository;

    public PublicacionFieldResolver(CanalPublicacionRepository canalPublicacionRepository) {
        this.canalPublicacionRepository = canalPublicacionRepository;
    }

    // Resolver del campo "canales" dentro de Publicacion
    @SchemaMapping(typeName = "Publicacion", field = "canales")
    public List<CanalInformacion> canales(Publicacion publicacion) {

        List<CanalPublicacion> relaciones =
                canalPublicacionRepository.findByPublicacion_IdPublicacion(publicacion.getIdPublicacion());

        return relaciones.stream()
                .map(CanalPublicacion::getCanal)
                .collect(Collectors.toList());
    }
}
