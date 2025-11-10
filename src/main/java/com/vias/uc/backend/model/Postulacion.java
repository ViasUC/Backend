package com.vias.uc.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Opción B:
 * - Conserva la columna física INTEGER `id_auditoria` sin relación JPA.
 * - Evita choques integer/bigint porque no hay JOIN automático.
 * - Cuando se crea la auditoría (BIGINT), se castea en el service con Math.toIntExact(...)
 *   y se almacena el valor entero en esta columna.
 */
@Entity
@Table(name = "postulaciones", schema = "public")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion")
    private Integer idPostulacion;

    @Column(name = "id_oportunidad", nullable = false)
    private Integer idOportunidad;

    @Column(name = "id_postulante", nullable = false)
    private Integer idPostulante; // usuarios.id_usuario (alumno)

    @Column(name = "id_ofertante", nullable = false)
    private Integer idOfertante;  // oportunidades.id_creador

    @Column(name = "fecha_postulacion", nullable = false)
    private LocalDateTime fechaPostulacion;

    @Column(name = "estado", nullable = false)
    private String estado; // 'pendiente' | 'aceptada' | 'rechazada'

    @Column(name = "motivo")
    private String motivo; // JSON: {"motivo":"...", "evidencias":[...]}

    // *** Clave: mantener INTEGER y SIN relación JPA ***
    @Column(name = "id_auditoria", nullable = false)
    private Integer idAuditoria;


    // ===== Getters & Setters =====
    public Integer getIdPostulacion() { return idPostulacion; }
    public void setIdPostulacion(Integer idPostulacion) { this.idPostulacion = idPostulacion; }

    public Integer getIdOportunidad() { return idOportunidad; }
    public void setIdOportunidad(Integer idOportunidad) { this.idOportunidad = idOportunidad; }

    public Integer getIdPostulante() { return idPostulante; }
    public void setIdPostulante(Integer idPostulante) { this.idPostulante = idPostulante; }

    public Integer getIdOfertante() { return idOfertante; }
    public void setIdOfertante(Integer idOfertante) { this.idOfertante = idOfertante; }

    public LocalDateTime getFechaPostulacion() { return fechaPostulacion; }
    public void setFechaPostulacion(LocalDateTime fechaPostulacion) { this.fechaPostulacion = fechaPostulacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Integer getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(Integer idAuditoria) { this.idAuditoria = idAuditoria; }

}
