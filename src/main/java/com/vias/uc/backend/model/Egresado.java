package com.vias.uc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "egresados", schema = "public")
public class Egresado {

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "anio_egreso")
    private Integer anioEgreso;

    @Column(name = "id_portafolio")
    private Integer idPortafolio;

    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public Integer getAnioEgreso() { return anioEgreso; }
    public void setAnioEgreso(Integer anioEgreso) { this.anioEgreso = anioEgreso; }

    public Integer getIdPortafolio() { return idPortafolio; }
    public void setIdPortafolio(Integer idPortafolio) { this.idPortafolio = idPortafolio; }

    public Integer getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(Integer idAuditoria) { this.idAuditoria = idAuditoria; }

    public Usuario getUsuario() { return usuario; }
}
