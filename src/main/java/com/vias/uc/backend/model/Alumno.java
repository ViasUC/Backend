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

    @Column(name = "carrera")
    private String carrera;

    @Column(name = "semestre")
    private Integer semestre;

    @Column(name = "id_auditoria")
    private Integer idAuditoria;
}
