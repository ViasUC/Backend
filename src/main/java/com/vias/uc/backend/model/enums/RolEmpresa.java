package com.vias.uc.backend.model.enums;

/**
 * Roles internos de usuarios dentro de una empresa
 * Estos roles determinan los permisos y accesos dentro del sistema
 */
public enum RolEmpresa {
    /**
     * Administrador de la empresa
     * - Primer usuario que crea la empresa
     * - Puede gestionar perfil de empresa
     * - Puede aprobar/rechazar usuarios
     * - Puede cambiar roles de otros usuarios
     * - Acceso completo a todas las funcionalidades
     */
    ADMINISTRADOR,
    
    /**
     * Gerente de Recursos Humanos
     * - Puede buscar candidatos
     * - Puede publicar ofertas
     * - Puede gestionar postulaciones
     * - NO puede modificar perfil de empresa
     * - NO puede gestionar usuarios
     */
    GERENTE_RRHH,
    
    /**
     * Auxiliar de Recursos Humanos
     * - Puede buscar candidatos
     * - Puede ver ofertas (solo lectura)
     * - Puede ver postulaciones (solo lectura)
     * - NO puede publicar ofertas
     * - NO puede modificar perfil
     * - NO puede gestionar usuarios
     */
    AUXILIAR_RRHH;
    
    /**
     * Obtiene el nombre legible del rol
     */
    public String getNombreAmigable() {
        return switch (this) {
            case ADMINISTRADOR -> "Administrador";
            case GERENTE_RRHH -> "Gerente de RRHH";
            case AUXILIAR_RRHH -> "Auxiliar de RRHH";
        };
    }
}
