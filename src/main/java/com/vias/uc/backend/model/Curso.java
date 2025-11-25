package com.vias.uc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_curso")
    private Integer idCurso;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "duracion")
    private String duracion;

    @Column(name = "requisitos")
    private String requisitos;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "modalidad")
    private String modalidad;

    @Column(name = "area")
    private String area;

    @Column(name = "enlace")
    private String enlace;

    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    // Getters y Setters
    public Integer getIdCurso() {
        return idCurso;
    }
    public void setIdCurso(Integer idCurso) {
        this.idCurso = idCurso;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }

    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getEnlace() { return enlace; }
    public void setEnlace(String enlace) { this.enlace = enlace; }

    public Integer getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(Integer idAuditoria) { this.idAuditoria = idAuditoria; }
}
