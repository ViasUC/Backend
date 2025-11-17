package com.vias.uc.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad Sesion - Registra las sesiones activas de los usuarios
 * Tabla: sesion
 */
@Entity
@Table(name = "sesion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sesion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    private Integer idSesion;
    
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;
    
    @Column(name = "fecha_ini")
    private LocalDateTime fechaIni;
    
    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;
    
    @Column(name = "id_auditoria")
    private Integer idAuditoria;
    
    /**
     * Verifica si la sesión está activa
     */
    public boolean isActiva() {
        return fechaFin == null || fechaFin.isAfter(LocalDateTime.now());
    }
}
