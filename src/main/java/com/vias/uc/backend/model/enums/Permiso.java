package com.vias.uc.backend.model.enums;

import java.util.Set;

/**
 * Permisos disponibles en el sistema para usuarios de empresa
 */
public enum Permiso {
    // Gestión de empresa
    EDITAR_PERFIL_EMPRESA,
    VER_PERFIL_EMPRESA,
    
    // Gestión de usuarios
    GESTIONAR_USUARIOS,
    APROBAR_USUARIOS,
    VER_USUARIOS,
    
    // Ofertas laborales
    CREAR_OFERTAS,
    EDITAR_OFERTAS,
    ELIMINAR_OFERTAS,
    VER_OFERTAS,
    
    // Postulaciones
    GESTIONAR_POSTULACIONES,
    VER_POSTULACIONES,
    
    // Candidatos
    BUSCAR_CANDIDATOS,
    VER_CANDIDATOS;
    
    /**
     * Obtiene los permisos según el rol de empresa
     */
    public static Set<Permiso> getPermisosPorRol(RolEmpresa rol) {
        return switch (rol) {
            case ADMINISTRADOR -> Set.of(
                // Todos los permisos
                EDITAR_PERFIL_EMPRESA,
                VER_PERFIL_EMPRESA,
                GESTIONAR_USUARIOS,
                APROBAR_USUARIOS,
                VER_USUARIOS,
                CREAR_OFERTAS,
                EDITAR_OFERTAS,
                ELIMINAR_OFERTAS,
                VER_OFERTAS,
                GESTIONAR_POSTULACIONES,
                VER_POSTULACIONES,
                BUSCAR_CANDIDATOS,
                VER_CANDIDATOS
            );
            
            case GERENTE_RRHH -> Set.of(
                VER_PERFIL_EMPRESA,
                VER_USUARIOS,
                CREAR_OFERTAS,
                EDITAR_OFERTAS,
                VER_OFERTAS,
                GESTIONAR_POSTULACIONES,
                VER_POSTULACIONES,
                BUSCAR_CANDIDATOS,
                VER_CANDIDATOS
            );
            
            case AUXILIAR_RRHH -> Set.of(
                VER_PERFIL_EMPRESA,
                VER_USUARIOS,
                VER_OFERTAS,
                VER_POSTULACIONES,
                BUSCAR_CANDIDATOS,
                VER_CANDIDATOS
            );
        };
    }
    
    /**
     * Verifica si un rol tiene un permiso específico
     */
    public static boolean tienePermiso(RolEmpresa rol, Permiso permiso) {
        return getPermisosPorRol(rol).contains(permiso);
    }
}
