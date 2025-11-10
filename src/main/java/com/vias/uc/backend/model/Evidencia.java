package com.vias.uc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "evidencia", schema = "public")
public class Evidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evidencia")
    private Integer idEvidencia;

    @Column(name = "id_portafolio", nullable = false)
    private Integer idPortafolio;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "recurso")
    private String recurso; // URL/ruta/identificador del recurso

    // Getters & Setters
    public Integer getIdEvidencia() { return idEvidencia; }
    public void setIdEvidencia(Integer idEvidencia) { this.idEvidencia = idEvidencia; }

    public Integer getIdPortafolio() { return idPortafolio; }
    public void setIdPortafolio(Integer idPortafolio) { this.idPortafolio = idPortafolio; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getRecurso() { return recurso; }
    public void setRecurso(String recurso) { this.recurso = recurso; }
}
