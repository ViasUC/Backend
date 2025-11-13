package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Investigador;
import com.vias.uc.backend.model.Profesor;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsuarioResolver {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;

    // === Queries ===
    @QueryMapping
    public List<Usuario> usuarios() {
        return usuarioRepository.findAll();
    }

    @QueryMapping
    public Usuario usuario(@Argument Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // === Mutations ===
    @MutationMapping
    public Profesor registrarProfesor(@Argument("input") UsuarioService.ProfesorInput input) {
        return usuarioService.registrarProfesor(input);
    }

    @MutationMapping
    public Investigador registrarInvestigador(@Argument("input") UsuarioService.InvestigadorInput input) {
        return usuarioService.registrarInvestigador(input);
    }

    @MutationMapping
    public Profesor actualizarProfesor(@Argument Integer id, @Argument("input") UsuarioService.ProfesorInput input) {
        return usuarioService.actualizarProfesor(id, input);
    }

    @MutationMapping
    public Investigador actualizarInvestigador(@Argument Integer id, @Argument("input") UsuarioService.InvestigadorInput input) {
        return usuarioService.actualizarInvestigador(id, input);
    }
}
