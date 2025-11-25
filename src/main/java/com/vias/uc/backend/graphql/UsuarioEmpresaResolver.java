package com.vias.uc.backend.graphql;

import com.vias.uc.backend.model.EmpresaUsuario;
import com.vias.uc.backend.model.Usuario;
import com.vias.uc.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

/**
 * Resolver GraphQL para el tipo UsuarioEmpresa
 * Maneja la carga del objeto Usuario relacionado
 */
@Controller
@RequiredArgsConstructor
public class UsuarioEmpresaResolver {

    private final UsuarioRepository usuarioRepository;

    /**
     * Resuelve el campo 'usuario' del tipo UsuarioEmpresa
     * Carga explícitamente el Usuario desde la BD usando el ID
     */
    @SchemaMapping(typeName = "UsuarioEmpresa", field = "usuario")
    public Usuario usuario(EmpresaUsuario empresaUsuario) {
        // Cargar el usuario desde la BD usando el ID
        return usuarioRepository.findById(empresaUsuario.getUsuario())
                .orElse(null);
    }

    /**
     * Resuelve el campo 'idEmpresa' del tipo UsuarioEmpresa
     */
    @SchemaMapping(typeName = "UsuarioEmpresa", field = "idEmpresa")
    public Integer idEmpresa(EmpresaUsuario empresaUsuario) {
        return empresaUsuario.getEmpresa();
    }

    /**
     * Resuelve el campo 'idUsuario' del tipo UsuarioEmpresa
     */
    @SchemaMapping(typeName = "UsuarioEmpresa", field = "idUsuario")
    public String idUsuario(EmpresaUsuario empresaUsuario) {
        return empresaUsuario.getUsuario().toString();
    }

    // ===== Resolvers para SolicitudAcceso =====
    
    /**
     * Resuelve el campo 'usuario' del tipo SolicitudAcceso
     * SolicitudAcceso también necesita cargar el Usuario
     */
    @SchemaMapping(typeName = "SolicitudAcceso", field = "usuario")
    public Usuario usuarioSolicitud(EmpresaUsuario empresaUsuario) {
        return usuarioRepository.findById(empresaUsuario.getUsuario())
                .orElse(null);
    }

    /**
     * Resuelve el campo 'idEmpresa' del tipo SolicitudAcceso
     */
    @SchemaMapping(typeName = "SolicitudAcceso", field = "idEmpresa")
    public Integer idEmpresaSolicitud(EmpresaUsuario empresaUsuario) {
        return empresaUsuario.getEmpresa();
    }

    /**
     * Resuelve el campo 'idUsuario' del tipo SolicitudAcceso
     */
    @SchemaMapping(typeName = "SolicitudAcceso", field = "idUsuario")
    public String idUsuarioSolicitud(EmpresaUsuario empresaUsuario) {
        return empresaUsuario.getUsuario().toString();
    }

    /**
     * Resuelve el campo 'rolSolicitado' del tipo SolicitudAcceso
     */
    @SchemaMapping(typeName = "SolicitudAcceso", field = "rolSolicitado")
    public String rolSolicitado(EmpresaUsuario empresaUsuario) {
        return empresaUsuario.getRolEnEmpresa().name();
    }

    /**
     * Resuelve el campo 'fechaSolicitud' del tipo SolicitudAcceso
     */
    @SchemaMapping(typeName = "SolicitudAcceso", field = "fechaSolicitud")
    public String fechaSolicitud(EmpresaUsuario empresaUsuario) {
        return empresaUsuario.getFechaAlta().toString();
    }
}
