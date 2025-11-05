package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsuarioResolver {

    private final UsuarioRepository usuarioRepository;

    @QueryMapping
    public List<Usuario> usuarios() {
        return usuarioRepository.findAll();
    }

    @QueryMapping
    public Usuario usuario(@Argument Long id) {
        return usuarioRepository.findById(Math.toIntExact(id)).orElse(null);
    }
}
