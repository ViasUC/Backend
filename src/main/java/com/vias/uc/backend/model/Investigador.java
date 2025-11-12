package com.vias.uc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "investigadores")
public class Investigador {

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "areas_investigacion")
    private String areasInvestigacion;

    private String afiliaciones;

    private Integer hindex;

    @Column(name = "id_auditoria", nullable = false)
    private Integer idAuditoria;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    // ======= Getters y Setters =======

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getAreasInvestigacion() {
        return areasInvestigacion;
    }

    public void setAreasInvestigacion(String areasInvestigacion) {
        this.areasInvestigacion = areasInvestigacion;
    }

    public String getAfiliaciones() {
        return afiliaciones;
    }

    public void setAfiliaciones(String afiliaciones) {
        this.afiliaciones = afiliaciones;
    }

    public Integer getHindex() {
        return hindex;
    }

    public void setHindex(Integer hindex) {
        this.hindex = hindex;
    }

    public Integer getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(Integer idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
