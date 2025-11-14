package com.vias.uc.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.vias.uc.backend.model.enums.EstadoPublicacion;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Table(name = "publicaciones")
public class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publicacion")
    private Integer idPublicacion;

    @Column(name = "id_reporte")
    private Integer idReporte; // FK opcional

    @Column(name = "id_proyecto")
    private Integer idProyecto; // FK opcional

    @Column(name = "publicado_por_profesor")
    private Integer publicadoPorProfesor; // FK a profesores.id_usuario

    @Column(name = "publicado_por_investigador")
    private Integer publicadoPorInvestigador; // FK a investigadores.id_usuario

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "estado", columnDefinition = "estado_publicacion")
    private EstadoPublicacion estado; // borrador | publicado | aprobado | rechazado

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Column(name = "fecha_aprobacion")
    private LocalDateTime fechaAprobacion;

    @Column(name = "observacion")
    private String observacion; // contenido del post (texto)

    @Column(name = "id_auditoria")
    private Integer idAuditoria; // FK a auditoria

    // GETTERS Y SETTERS

    public Integer getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Integer idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public Integer getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Integer idReporte) {
        this.idReporte = idReporte;
    }

    public Integer getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(Integer idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Integer getPublicadoPorProfesor() {
        return publicadoPorProfesor;
    }

    public void setPublicadoPorProfesor(Integer publicadoPorProfesor) {
        this.publicadoPorProfesor = publicadoPorProfesor;
    }

    public Integer getPublicadoPorInvestigador() {
        return publicadoPorInvestigador;
    }

    public void setPublicadoPorInvestigador(Integer publicadoPorInvestigador) {
        this.publicadoPorInvestigador = publicadoPorInvestigador;
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPublicacion estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public LocalDateTime getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(LocalDateTime fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(Integer idAuditoria) {
        this.idAuditoria = idAuditoria;
    }
}
