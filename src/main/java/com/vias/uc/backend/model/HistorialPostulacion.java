package com.vias.uc.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_postulacion")
public class HistorialPostulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Long idHistorial;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_postulacion", nullable = false)
    private Postulacion postulacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", length = 20)
    private Postulacion.EstadoPostulacion estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", length = 20, nullable = false)
    private Postulacion.EstadoPostulacion estadoNuevo;

    @Column(name = "motivo", length = 500)
    private String motivo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio = LocalDateTime.now();

    public Long getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Long idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Postulacion getPostulacion() {
        return postulacion;
    }

    public void setPostulacion(Postulacion postulacion) {
        this.postulacion = postulacion;
    }

    public Postulacion.EstadoPostulacion getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(Postulacion.EstadoPostulacion estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public Postulacion.EstadoPostulacion getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(Postulacion.EstadoPostulacion estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}
