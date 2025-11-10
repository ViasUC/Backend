package com.vias.uc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "oportunidades", schema = "public")
public class Oportunidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oportunidad")
    private Integer idOportunidad;

    @Column(name = "id_creador", nullable = false)
    private Integer idCreador; // usuarios.id_usuario (docente/no-alumno)

    @Column(name = "titulo")
    private String titulo;

    // (Opcionales según tu tabla)
    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "estado")
    private String estado; // 'activo','borrador','pausada','cerrado', etc.

    // Getters & Setters
    public Integer getIdOportunidad() { return idOportunidad; }
    public void setIdOportunidad(Integer idOportunidad) { this.idOportunidad = idOportunidad; }

    public Integer getIdCreador() { return idCreador; }
    public void setIdCreador(Integer idCreador) { this.idCreador = idCreador; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
