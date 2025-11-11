package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.UsuarioRepository;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UsuarioQueryResolver {

    private final UsuarioRepository usuarioRepository;

    public UsuarioQueryResolver(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @QueryMapping
    public Usuario usuarioById(@Argument Integer id) {
    return usuarioRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Usuario> usuarios() {
        return usuarioRepository.findAll();
    }

}
