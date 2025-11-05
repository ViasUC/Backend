package com.vias.uc.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "postulaciones")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion")
    private Integer idPostulacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_oportunidad", nullable = false)
    private Oportunidad oportunidad;

    // Usuario que postula (obligatorio)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_postulante", nullable = false)
    private Usuario postulante;

    // Usuario ofertante (creador de la oportunidad, obligatorio)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ofertante", nullable = false)
    private Usuario ofertante;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPostulacion estado;

    @Column(name = "fecha_postulacion", nullable = false)
    private LocalDateTime fechaPostulacion;

    // Auditoría obligatoria (columna NOT NULL)
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "id_auditoria",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_postulaciones_auditoria")
    )
    private Auditoria auditoria;

    public Postulacion() { }

    public Postulacion(Alumno alumno, Oportunidad oportunidad, Usuario postulante, EstadoPostulacion estado) {
        this.alumno = alumno;
        this.oportunidad = oportunidad;
        this.postulante = postulante;
        this.estado = estado;
        this.fechaPostulacion = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaPostulacion == null) this.fechaPostulacion = LocalDateTime.now();
        if (this.postulante == null && this.alumno != null) this.postulante = this.alumno.getUsuario();
        // La auditoría se setea desde el service (obligatoria)
    }

    // ===== Getters & Setters =====
    public Integer getIdPostulacion() { return idPostulacion; }

    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public Oportunidad getOportunidad() { return oportunidad; }
    public void setOportunidad(Oportunidad oportunidad) { this.oportunidad = oportunidad; }

    public Usuario getPostulante() { return postulante; }
    public void setPostulante(Usuario postulante) { this.postulante = postulante; }

    public Usuario getOfertante() { return ofertante; }
    public void setOfertante(Usuario ofertante) { this.ofertante = ofertante; }

    public EstadoPostulacion getEstado() { return estado; }
    public void setEstado(EstadoPostulacion estado) { this.estado = estado; }

    public LocalDateTime getFechaPostulacion() { return fechaPostulacion; }
    public void setFechaPostulacion(LocalDateTime fechaPostulacion) { this.fechaPostulacion = fechaPostulacion; }

    public Auditoria getAuditoria() { return auditoria; }
    public void setAuditoria(Auditoria auditoria) { this.auditoria = auditoria; }
}
