package com.vias.uc.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "alumnos")
public class Alumno {

    @Id
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario; // 👈 relación con la tabla usuarios

    @Column(nullable = false)
    private String carrera;

    @Column(nullable = false)
    private Integer semestre;

    @Column(name = "id_auditoria", nullable = false)
    private Integer idAuditoria;
}
