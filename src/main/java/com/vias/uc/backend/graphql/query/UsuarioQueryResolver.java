package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.service.UsuarioService;
import com.vias.uc.backend.model.dto.UsuarioInput;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class UsuarioQueryResolver {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    public UsuarioQueryResolver(
            UsuarioRepository usuarioRepository,
            UsuarioService usuarioService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @QueryMapping
    public Usuario usuarioById(@Argument Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @QueryMapping
    public List<Usuario> usuarios() {
        return usuarioRepository.findAll();
    }

    @MutationMapping
    public Usuario actualizarUsuario(
            @Argument Integer idUsuario,
            @Argument UsuarioInput input
    ) {
        return usuarioService.actualizarUsuario(idUsuario, input);
    }
}
