package com.vias.uc.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_postulaciones")
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
    private EstadoPostulacion estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false, length = 20)
    private EstadoPostulacion estadoNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @Column(name = "motivo")
    private String motivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_actor")
    private Usuario actor;

    @PrePersist
    void onCreate() {
        if (fechaCambio == null) fechaCambio = LocalDateTime.now();
    }

    public Long getIdHistorial() { return idHistorial; }
    public Postulacion getPostulacion() { return postulacion; }
    public void setPostulacion(Postulacion postulacion) { this.postulacion = postulacion; }
    public EstadoPostulacion getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(EstadoPostulacion estadoAnterior) { this.estadoAnterior = estadoAnterior; }
    public EstadoPostulacion getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(EstadoPostulacion estadoNuevo) { this.estadoNuevo = estadoNuevo; }
    public LocalDateTime getFechaCambio() { return fechaCambio; }
    public void setFechaCambio(LocalDateTime fechaCambio) { this.fechaCambio = fechaCambio; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public Usuario getActor() { return actor; }
    public void setActor(Usuario actor) { this.actor = actor; }
}
