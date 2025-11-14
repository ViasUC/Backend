package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Conexion;
import com.vias.uc.backend.repository.ConexionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ConexionResolver {

    @Autowired
    private ConexionRepository conexionRepository;

    @QueryMapping
    public List<Conexion> conexionesPorUsuario(@Argument Integer idUsuario) {

        System.out.println(">>> QUERY conexionesPorUsuario llamada con idUsuario=" + idUsuario);

        List<Conexion> conexiones = conexionRepository.findByIdUsuario1OrIdUsuario2(idUsuario, idUsuario);

        System.out.println(">>> Conexiones encontradas: " + conexiones.size());

        return conexiones;
    }
}
