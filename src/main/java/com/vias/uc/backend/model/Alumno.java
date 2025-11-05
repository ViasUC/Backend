package com.vias.uc.backend.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "alumnos")
public class Alumno implements Serializable {

    // PK compartida con Usuario: NO usar @GeneratedValue aquí
    @Id
    @Column(name = "id_alumno")
    private Integer idAlumno;

    // Toma el id desde Usuario.idUsuario (PK compartida)
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(
            name = "id_alumno",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_alumnos_usuarios")
    )
    private Usuario usuario;

    @Column(name = "carrera")
    private String carrera;

    @Column(name = "semestre")
    private Integer semestre;

    // Auditoría obligatoria (id_auditoria NOT NULL).
    // ManyToOne es más flexible (sin UNIQUE implícito).
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "id_auditoria",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_alumnos_auditoria")
    )
    private Auditoria auditoria;

    // ===== getters/setters =====
    public Integer getIdAlumno() { return idAlumno; }

    // Podés hacerlo package-private si querés limitar su uso.
    public void setIdAlumno(Integer idAlumno) { this.idAlumno = idAlumno; }

    public Usuario getUsuario() { return usuario; }

    public void setUsuario(Usuario usuario) {
        // Dejar que @MapsId copie la PK en persist; no sincronizar manualmente.
        this.usuario = usuario;
    }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }

    public Auditoria getAuditoria() { return auditoria; }
    public void setAuditoria(Auditoria auditoria) { this.auditoria = auditoria; }
}
