package com.vias.uc.backend.model;

import jakarta.persistence.*;
import java.io.Serializable;

// Hibernate 6
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "alumnos")
public class Alumno implements Serializable {

    // La PK de alumnos es la MISMA que la de Usuario (compartida)
    // En BD la columna es INT4, pero Usuario usa BIGINT.
    // Forzamos la validación a INTEGER con @JdbcTypeCode para que no falle.
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

    // ===== getters/setters =====
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }

    public Auditoria getAuditoria() { return auditoria; }
    public void setAuditoria(Auditoria auditoria) { this.auditoria = auditoria; }
}
