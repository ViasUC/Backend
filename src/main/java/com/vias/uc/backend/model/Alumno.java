package com.vias.uc.backend.model;

import java.math.BigInteger;

import jakarta.persistence.*;
import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "alumnos", schema = "public")
public class Alumno {

    @Id
    @JdbcTypeCode(SqlTypes.INTEGER)                 // <- valida contra int4 de la tabla
    @Column(name = "id_usuario", nullable = false)  // <- columna REAL en la BD
    private Long idUsuario;                         // <- tipo Java Long para alinear con Usuario

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId                                         // <- comparte la PK con Usuario
    @JoinColumn(name = "id_usuario", nullable = false,
            foreignKey = @ForeignKey(name = "fk_alumnos_usuarios"))
    private Usuario usuario;

    @Column(name = "carrera", length = 255)
    private String carrera;

    @Column(name = "semestre")
    private Integer semestre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_auditoria", nullable = false,
            foreignKey = @ForeignKey(name = "fk_alumnos_auditoria"))
    private Auditoria auditoria;

}
