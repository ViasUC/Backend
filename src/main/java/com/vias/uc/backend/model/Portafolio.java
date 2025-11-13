package com.vias.uc.backend.model;

import java.time.OffsetDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "portafolio", schema = "public")
public class Portafolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_portafolio")
    private Integer idPortafolio;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    private String descripcion;

    private String skills;

    @Column(name = "visibilidad")
    private Boolean visibilidad;

    @Column(name = "ultima_actualizacion")
    private java.time.OffsetDateTime ultimaActualizacion;


    @Column(name = "id_auditoria")
    private Integer idAuditoria;


    // GETTERS y SETTERS

    public Integer getIdPortafolio() {
        return idPortafolio;
    }

    public void setIdPortafolio(Integer idPortafolio) {
        this.idPortafolio = idPortafolio;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public Boolean getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(Boolean visibilidad) {
        this.visibilidad = visibilidad;
    }

    public OffsetDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(OffsetDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }


    public Integer getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(Integer idAuditoria) {
        this.idAuditoria = idAuditoria;
    }
}
