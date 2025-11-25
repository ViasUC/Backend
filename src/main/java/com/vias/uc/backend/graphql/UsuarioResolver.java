package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.Investigador;
import com.vias.uc.backend.model.Portafolio;
import com.vias.uc.backend.model.Profesor;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.model.dto.InvestigadorData;
import com.vias.uc.backend.model.dto.ProfesorData;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UsuarioResolver {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final PortafolioRepository portafolioRepository;

    // === Queries ===
    @QueryMapping
    public List<Usuario> usuarios() {
        return usuarioRepository.findAll();
    }

    @QueryMapping
    public Usuario usuario(@Argument Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // === Field Resolvers ===
    @SchemaMapping(typeName = "Usuario", field = "portafolio")
    public Portafolio portafolio(Usuario usuario) {
        return portafolioRepository.findByIdUsuario(usuario.getIdUsuario()).orElse(null);
    }

    // === Mutations ===
    @MutationMapping
    public Profesor registrarProfesor(@Argument Integer idActor,
                                      @Argument("input") UsuarioService.ProfesorInput input) {
        return usuarioService.registrarProfesor(idActor, input);
    }

    @MutationMapping
    public Investigador registrarInvestigador(@Argument Integer idActor,
                                              @Argument("input") UsuarioService.InvestigadorInput input) {
        return usuarioService.registrarInvestigador(idActor, input);
    }


    @MutationMapping
    public Profesor actualizarProfesor(@Argument Integer id, @Argument("input") UsuarioService.ProfesorInput input) {
        return usuarioService.actualizarProfesor(id, input);
    }

    @MutationMapping
    public Investigador actualizarInvestigador(@Argument Integer id, @Argument("input") UsuarioService.InvestigadorInput input) {
        return usuarioService.actualizarInvestigador(id, input);
    }

    @QueryMapping
    public ProfesorData obtenerProfesorConUsuario(@Argument Long idProfesor) {
        // Obtener datos de Usuario
        Usuario usuario = usuarioRepository.findById(idProfesor).orElse(null);

        // Obtener datos de Profesor
        Profesor profesor = usuarioService.obtenerProfesorPorUsuarioId(Math.toIntExact(idProfesor));

        if (usuario == null || profesor == null) {
            return null;
        }

        // Crear y devolver el objeto ProfesorData
        return new ProfesorData(usuario, profesor);
    }

    @QueryMapping
    public InvestigadorData obtenerInvestigadorConUsuario(@Argument Long idInvestigador) {
        // Obtener datos de Usuario
        Usuario usuario = usuarioRepository.findById(idInvestigador).orElse(null);

        // Obtener datos de Investigador
        Investigador investigador = usuarioService.obtenerInvestigadorPorUsuarioId(Math.toIntExact(idInvestigador));

        if (usuario == null || investigador == null) {
            return null;
        }

        // Devolver el objeto InvestigadorData con Usuario e Investigador
        return new InvestigadorData(usuario, investigador);
    }



}
