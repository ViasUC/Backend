package com.vias.uc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "postulacion_evidencia")
public class PostulacionEvidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion_evidencia")
    private Integer idPostulacionEvidencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_postulacion",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_pe_postulacion")
    )
    private Postulacion postulacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_evidencia",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_pe_evidencia")
    )
    private Evidencia evidencia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "id_auditoria",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_pe_auditoria")
    )
    private Auditoria auditoria;

    // ===== Getters & Setters =====

    public Integer getIdPostulacionEvidencia() {
        return idPostulacionEvidencia;
    }

    public void setIdPostulacionEvidencia(Integer idPostulacionEvidencia) {
        this.idPostulacionEvidencia = idPostulacionEvidencia;
    }

    public Postulacion getPostulacion() {
        return postulacion;
    }

    public void setPostulacion(Postulacion postulacion) {
        this.postulacion = postulacion;
    }

    public Evidencia getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(Evidencia evidencia) {
        this.evidencia = evidencia;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }
}
