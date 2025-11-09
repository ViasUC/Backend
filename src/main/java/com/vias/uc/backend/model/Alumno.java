package com.vias.uc.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "alumnos", schema = "public")
public class Alumno {

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(nullable = false)
    private String carrera;

    @Column(nullable = false)
    private Integer semestre;

    @Column(name = "id_auditoria", nullable = false)
    private Integer idAuditoria;
}