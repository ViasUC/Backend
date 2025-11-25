package com.vias.uc.backend.graphql;

import com.vias.uc.backend.graphql.dto.CrearOportunidadInput;
import com.vias.uc.backend.model.Oportunidad;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.OportunidadRepository;
import com.vias.uc.backend.repository.UsuarioRepository;
import com.vias.uc.backend.service.OportunidadService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OportunidadResolver {

    private final OportunidadService oportunidadService;
    private final OportunidadRepository oportunidadRepository;
    private final UsuarioRepository usuarioRepository;

    public OportunidadResolver(OportunidadService oportunidadService,
                               OportunidadRepository oportunidadRepository,
                               UsuarioRepository usuarioRepository) {
        this.oportunidadService = oportunidadService;
        this.oportunidadRepository = oportunidadRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ===== Queries =====

    @QueryMapping
    public List<Oportunidad> oportunidades() {
        return oportunidadRepository.findAll();
    }

    @QueryMapping
    public Oportunidad oportunidad(@Argument Integer id) {
        return oportunidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oportunidad no encontrada: " + id));
    }

    @QueryMapping
    public List<Oportunidad> oportunidadesPorCreador(@Argument Long creadorId) {
        return oportunidadService.porCreador(creadorId);
    }

    @QueryMapping
    public List<Oportunidad> oportunidadesPorEmpresa(@Argument Long idEmpresa) {
        return oportunidadRepository.findAllByEmpresaId(idEmpresa);
    }

    // ===== Mutations =====

    @MutationMapping
    public Oportunidad crearOportunidadEmpresa(@Argument("input") CrearOportunidadInput input) {
        return oportunidadService.crearOportunidadEmpresa(input);
    }

    @MutationMapping
    public Oportunidad actualizarOportunidad(@Argument Integer id,
                                             @Argument("input") CrearOportunidadInput input,
                                             @Argument Long idActor) {
        return oportunidadService.actualizarOportunidad(id, input, idActor);
    }

    @MutationMapping
    public Oportunidad cambiarEstadoOportunidad(@Argument Integer id,
                                                @Argument String estado,
                                                @Argument Long idActor) {
        // El servicio valida y normaliza el estado
        return oportunidadService.cambiarEstado(id, estado, idActor);
    }

    @MutationMapping
    public Boolean eliminarOportunidad(@Argument Integer id,
                                       @Argument Long idActor) {
        return oportunidadService.eliminar(id, idActor);
    }

    // ===== Field resolvers =====

    @SchemaMapping(typeName = "Oportunidad", field = "creador")
    public Usuario resolverCreador(Oportunidad oportunidad) {
        if (oportunidad.getIdCreador() == null) {
            return null;
        }
        return usuarioRepository.findById(oportunidad.getIdCreador().longValue())
                .orElseThrow(() ->
                        new RuntimeException("Creador no encontrado: " + oportunidad.getIdCreador()));
    }

    @SchemaMapping(typeName = "Oportunidad", field = "id")
    public Integer getId(Oportunidad oportunidad) {
        return oportunidad.getIdOportunidad();
    }

    @SchemaMapping(typeName = "Oportunidad", field = "empresa")
    public String resolverEmpresa(Oportunidad oportunidad) {
        // Por ahora retornamos null ya que el campo 'empresa' en el schema es String
        // y no tenemos la relación completamente configurada
        return null;
    }
}
