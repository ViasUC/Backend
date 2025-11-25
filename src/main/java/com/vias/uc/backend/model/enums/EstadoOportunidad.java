package com.vias.uc.backend.model.enums;

/**
 * Enum que coincide exactamente con la constraint de PostgreSQL:
 * oportunidades_estado_check: ('activo', 'borrador', 'pausada', 'cerrado')
 */
public enum EstadoOportunidad {
    activo,
    borrador,
    pausada,
    cerrado
}
