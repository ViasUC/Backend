package com.vias.uc.backend.service;

import com.vias.uc.backend.model.enums.Permiso;
import com.vias.uc.backend.model.enums.RolEmpresa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio de autorización para validar permisos de usuarios de empresa
 * según su rol interno
 */
@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final UsuarioEmpresaService usuarioEmpresaService;
    private final SesionService sesionService;

    /**
     * Verifica si un usuario tiene un permiso específico en una empresa
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario
     * @param permiso Permiso a verificar
     * @return true si el usuario tiene el permiso, false en caso contrario
     */
    public boolean tienePermiso(Integer idEmpresa, Long idUsuario, Permiso permiso) {
        RolEmpresa rol = usuarioEmpresaService.obtenerRolUsuario(idEmpresa, idUsuario);
        
        if (rol == null) {
            return false; // Usuario no pertenece a la empresa
        }
        
        return Permiso.tienePermiso(rol, permiso);
    }

    /**
     * Verifica que un usuario tenga un permiso específico, lanzando excepción si no lo tiene
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario
     * @param permiso Permiso requerido
     * @throws SecurityException si el usuario no tiene el permiso
     */
    public void verificarPermiso(Integer idEmpresa, Long idUsuario, Permiso permiso) {
        if (!tienePermiso(idEmpresa, idUsuario, permiso)) {
            throw new SecurityException("No tienes permisos para realizar esta acción");
        }
    }

    /**
     * Verifica si un usuario es administrador de una empresa
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario
     * @return true si es administrador activo, false en caso contrario
     */
    public boolean esAdministrador(Integer idEmpresa, Long idUsuario) {
        return usuarioEmpresaService.esAdministrador(idEmpresa, idUsuario);
    }

    /**
     * Verifica que un usuario sea administrador, lanzando excepción si no lo es
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario
     * @throws SecurityException si el usuario no es administrador
     */
    public void verificarEsAdministrador(Integer idEmpresa, Long idUsuario) {
        if (!esAdministrador(idEmpresa, idUsuario)) {
            throw new SecurityException("Solo los administradores pueden realizar esta acción");
        }
    }

    /**
     * Obtiene el rol de un usuario en una empresa
     * 
     * @param idEmpresa ID de la empresa
     * @param idUsuario ID del usuario
     * @return El rol del usuario, o null si no pertenece a la empresa
     */
    public RolEmpresa obtenerRol(Integer idEmpresa, Long idUsuario) {
        return usuarioEmpresaService.obtenerRolUsuario(idEmpresa, idUsuario);
    }

    /**
     * Obtiene el ID de usuario desde el token de autorización
     * 
     * @param authHeader Header de autorización (Bearer token)
     * @return ID del usuario o null si el token es inválido
     */
    public Long getUserIdFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        
        String token = authHeader.substring(7);
        
        // Verificar si el token es válido a través del servicio de sesión
        try {
            return sesionService.getUserIdFromToken(token);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifica si un usuario es de tipo empresa
     * 
     * @param usuarioId ID del usuario
     * @return true si el usuario es de tipo empresa, false en caso contrario
     */
    public boolean isEmpresa(Long usuarioId) {
        try {
            // Verificar si tiene relación con alguna empresa activa
            return usuarioEmpresaService.perteneceAEmpresa(usuarioId);
        } catch (Exception e) {
            return false;
        }
    }
}
