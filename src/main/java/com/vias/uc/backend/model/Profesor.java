package com.vias.uc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profesores")
public class Profesor {

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    private String departamento;

    @Column(name = "categoria_docente")
    private String categoriaDocente;

    @Column(name = "areas_docentes")
    private String areasDocentes;

    @Column(name = "id_auditoria", nullable = false)
    private Integer idAuditoria;

    // --- relaciones opcionales ---
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

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCategoriaDocente() {
        return categoriaDocente;
    }

    public void setCategoriaDocente(String categoriaDocente) {
        this.categoriaDocente = categoriaDocente;
    }

    public String getAreasDocentes() {
        return areasDocentes;
    }

    public void setAreasDocentes(String areasDocentes) {
        this.areasDocentes = areasDocentes;
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
