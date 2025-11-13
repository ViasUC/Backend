package com.vias.uc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "administradores", schema = "public")
public class Administrador {

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    // Relación con Usuario
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(Integer idAuditoria) { this.idAuditoria = idAuditoria; }

    public Usuario getUsuario() { return usuario; }
}
