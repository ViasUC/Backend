package com.vias.uc.backend.graphql.mutation;

import com.vias.uc.backend.model.EmpresaUsuario;
import com.vias.uc.backend.model.enums.RolEmpresa;
import com.vias.uc.backend.service.UsuarioEmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * Resolver GraphQL para mutations relacionadas con la gestión de usuarios de empresa
 */
@Controller
@RequiredArgsConstructor
public class UsuarioEmpresaMutationResolver {

    private final UsuarioEmpresaService usuarioEmpresaService;

    /**
     * Solicita acceso de un usuario a una empresa con un rol específico
     */
    @MutationMapping
    public EmpresaUsuario solicitarAccesoEmpresa(@Argument Map<String, Object> input) {
        Integer idEmpresa = (Integer) input.get("idEmpresa");
        Long idUsuario = Long.valueOf(input.get("idUsuario").toString());
        RolEmpresa rolSolicitado = RolEmpresa.valueOf(input.get("rolSolicitado").toString());
        
        return usuarioEmpresaService.solicitarAccesoEmpresa(idEmpresa, idUsuario, rolSolicitado);
    }

    /**
     * Aprueba la solicitud de un usuario
     */
    @MutationMapping
    public EmpresaUsuario aprobarUsuarioEmpresa(@Argument Map<String, Object> input) {
        Integer idEmpresa = (Integer) input.get("idEmpresa");
        Long idUsuario = Long.valueOf(input.get("idUsuario").toString());
        Long idAdministrador = Long.valueOf(input.get("idAdministrador").toString());
        
        return usuarioEmpresaService.aprobarUsuario(idEmpresa, idUsuario, idAdministrador);
    }

    /**
     * Rechaza la solicitud de un usuario
     */
    @MutationMapping
    public Boolean rechazarUsuarioEmpresa(@Argument Map<String, Object> input) {
        Integer idEmpresa = (Integer) input.get("idEmpresa");
        Long idUsuario = Long.valueOf(input.get("idUsuario").toString());
        Long idAdministrador = Long.valueOf(input.get("idAdministrador").toString());
        
        usuarioEmpresaService.rechazarUsuario(idEmpresa, idUsuario, idAdministrador);
        return true;
    }

    /**
     * Cambia el rol de un usuario en la empresa
     */
    @MutationMapping
    public EmpresaUsuario cambiarRolUsuarioEmpresa(@Argument Map<String, Object> input) {
        Integer idEmpresa = (Integer) input.get("idEmpresa");
        Long idUsuario = Long.valueOf(input.get("idUsuario").toString());
        RolEmpresa nuevoRol = RolEmpresa.valueOf(input.get("nuevoRol").toString());
        Long idAdministrador = Long.valueOf(input.get("idAdministrador").toString());
        
        return usuarioEmpresaService.cambiarRolUsuario(idEmpresa, idUsuario, nuevoRol, idAdministrador);
    }

    /**
     * Desactiva un usuario de la empresa
     */
    @MutationMapping
    public Boolean desactivarUsuarioEmpresa(@Argument Map<String, Object> input) {
        Integer idEmpresa = (Integer) input.get("idEmpresa");
        Long idUsuario = Long.valueOf(input.get("idUsuario").toString());
        Long idAdministrador = Long.valueOf(input.get("idAdministrador").toString());
        
        usuarioEmpresaService.desactivarUsuario(idEmpresa, idUsuario, idAdministrador);
        return true;
    }
}
