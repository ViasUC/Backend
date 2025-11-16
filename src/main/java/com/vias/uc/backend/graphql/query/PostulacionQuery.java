package com.vias.uc.backend.graphql.query;
import com.vias.uc.backend.model.dto.PostulanteDTO;


import com.vias.uc.backend.model.Postulacion;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.PostulacionRepository;
import com.vias.uc.backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostulacionQuery {

    private final PostulacionRepository repo;
    private final UsuarioRepository usuarioRepo;

    // =========================================================
    // 🔵 Función original: postulaciones hechas por un usuario
    // =========================================================
    @QueryMapping
    public List<Postulacion> postulacionesPorUsuario(@Argument Integer idUsuario) {
        return repo.findByIdPostulante(idUsuario);
    }


    // =========================================================
    // 🔵 NUEVA FUNCIÓN: postulantes de una oportunidad
    // =========================================================
    @QueryMapping
    public List<PostulanteDTO> postulantesPorOportunidad(@Argument Integer idOportunidad) {

        System.out.println("📌 Consultando postulantes de la oportunidad: " + idOportunidad);

        // 1️⃣ Buscar postulaciones que coincidan
        List<Postulacion> lista = repo.findByIdOportunidad(idOportunidad);

System.out.println("🔎 findByIdOportunidad(" + idOportunidad + ") devolvió: " + lista.size());
lista.forEach(p -> System.out.println(" - Postulación → " + p.getIdPostulacion()
        + " | idPostulante=" + p.getIdPostulante()));

        // 2️⃣ Convertir cada Postulacion → DTO con Usuario + fecha
        return lista.stream()
                .map(p -> {
                    Usuario u = usuarioRepo.findById(p.getIdPostulante()).orElse(null);
                    if (u == null) return null;

                    PostulanteDTO dto = new PostulanteDTO();
                    dto.setIdUsuario(u.getIdUsuario());
                    dto.setNombre(u.getNombre());
                    dto.setApellido(u.getApellido());
                    dto.setEmail(u.getEmail());
                    dto.setTelefono(u.getTelefono());
                    dto.setUbicacion(u.getUbicacion());
                    dto.setRolPrincipal(u.getRolPrincipal().name());
                    dto.setCompletitud(u.getCompletitud());
                    dto.setFechaPostulacion(p.getFechaPostulacion());

                    return dto;
                })
                .filter(x -> x != null)
                .collect(Collectors.toList());
    }

}
