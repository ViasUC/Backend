package com.vias.uc.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "postulaciones")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion")
    private Long idPostulacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_oportunidad", nullable = false)
    private Oportunidad oportunidad;

    // 👇 NUEVO: el usuario que postula (no nulo en BD)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_postulante", nullable = false)
    private Usuario postulante;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPostulacion estado;

    @Column(name = "fecha_postulacion", nullable = false)
    private LocalDateTime fechaPostulacion;

    public Postulacion() {}

    // Constructor útil si querés crear rápido
    public Postulacion(Alumno alumno, Oportunidad oportunidad, Usuario postulante, EstadoPostulacion estado) {
        this.alumno = alumno;
        this.oportunidad = oportunidad;
        this.postulante = postulante;
        this.estado = estado;
        this.fechaPostulacion = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (this.fechaPostulacion == null) {
            this.fechaPostulacion = LocalDateTime.now();
        }
        // fallback por si el service no lo setea (evita NULL)
        if (this.postulante == null && this.alumno != null) {
            this.postulante = this.alumno.getUsuario();
        }
    }

    // ===== Getters & Setters =====
    public Long getIdPostulacion() { return idPostulacion; }
    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }

    public Oportunidad getOportunidad() { return oportunidad; }
    public void setOportunidad(Oportunidad oportunidad) { this.oportunidad = oportunidad; }

    public Usuario getPostulante() { return postulante; }
    public void setPostulante(Usuario postulante) { this.postulante = postulante; }

    public EstadoPostulacion getEstado() { return estado; }
    public void setEstado(EstadoPostulacion estado) { this.estado = estado; }

    public LocalDateTime getFechaPostulacion() { return fechaPostulacion; }
    public void setFechaPostulacion(LocalDateTime fechaPostulacion) { this.fechaPostulacion = fechaPostulacion; }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ofertante", nullable = false)
    private Usuario ofertante;

    public Usuario getOfertante() { return ofertante; }
    public void setOfertante(Usuario ofertante) { this.ofertante = ofertante; }

}
