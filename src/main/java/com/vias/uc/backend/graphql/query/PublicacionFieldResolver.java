package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.canales.CanalInformacion;
import com.vias.uc.backend.model.canales.CanalPublicacion;
import com.vias.uc.backend.repository.canales.CanalPublicacionRepository;
import com.vias.uc.backend.model.Publicacion;

import graphql.schema.DataFetcher;
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
        // Obtener la lista de relaciones entre publicación y canal
        List<CanalPublicacion> relaciones = canalPublicacionRepository.findByPublicacion_IdPublicacion(publicacion.getIdPublicacion());

        // Convertir las relaciones a una lista de canales
        return relaciones.stream()
                .map(CanalPublicacion::getCanal) // Aquí obtenemos el Canal a través de la relación
                .collect(Collectors.toList());
    }

    // Resolver del campo 'destacado' dentro de Publicacion
    @SchemaMapping(typeName = "Publicacion", field = "destacado")
    public Boolean destacado(Publicacion publicacion) {
        // Obtener el idCanal de los canales asociados a esta publicación
        List<CanalPublicacion> relaciones = canalPublicacionRepository.findByPublicacion_IdPublicacion(publicacion.getIdPublicacion());

        // Si no hay relaciones, no se puede obtener 'destacado'
        if (relaciones.isEmpty()) {
            return false;
        }

        // Suponiendo que quieras obtener el 'destacado' del primer canal relacionado
        CanalPublicacion canalPublicacion = relaciones.get(0); // Aquí puedes decidir cuál canal usar si hay más de uno

        // Log para verificar la relación
        System.out.println("CanalPublicacion: " + canalPublicacion);

        if (canalPublicacion != null) {
            return canalPublicacion.isDestacado(); // Retorna el valor de 'destacado'
        }

        return false;  // Si no se encuentra la relación, retorna false por defecto
    }

}
