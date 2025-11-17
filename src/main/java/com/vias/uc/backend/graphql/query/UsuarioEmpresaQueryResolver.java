package com.vias.uc.backend.graphql.query;

import com.vias.uc.backend.model.EmpresaUsuario;
import com.vias.uc.backend.model.enums.RolEmpresa;
import com.vias.uc.backend.service.UsuarioEmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Resolver GraphQL para queries relacionadas con la gestión de usuarios de empresa
 */
@Controller
@RequiredArgsConstructor
public class UsuarioEmpresaQueryResolver {

    private final UsuarioEmpresaService usuarioEmpresaService;

    /**
     * Lista todos los usuarios activos de una empresa
     */
    @QueryMapping
    public List<EmpresaUsuario> listarUsuariosEmpresa(@Argument Integer idEmpresa) {
        return usuarioEmpresaService.listarUsuariosEmpresa(idEmpresa);
    }

    /**
     * Lista todas las solicitudes pendientes de aprobación
     * En GraphQL se retorna como [SolicitudAcceso!]! pero en Java es EmpresaUsuario
     */
    @QueryMapping
    public List<EmpresaUsuario> listarSolicitudesPendientes(@Argument Integer idEmpresa) {
        return usuarioEmpresaService.listarSolicitudesPendientes(idEmpresa);
    }

    /**
     * Obtiene el rol de un usuario en una empresa
     */
    @QueryMapping
    public RolEmpresa obtenerRolUsuario(@Argument Integer idEmpresa, @Argument Long idUsuario) {
        System.out.println(">>> Query obtenerRolUsuario recibido");
        System.out.println(">>> idEmpresa: " + idEmpresa + " (tipo: " + (idEmpresa != null ? idEmpresa.getClass().getSimpleName() : "null") + ")");
        System.out.println(">>> idUsuario: " + idUsuario + " (tipo: " + (idUsuario != null ? idUsuario.getClass().getSimpleName() : "null") + ")");
        
        RolEmpresa rol = usuarioEmpresaService.obtenerRolUsuario(idEmpresa, idUsuario);
        System.out.println(">>> Rol encontrado: " + rol);
        
        return rol;
    }
}
