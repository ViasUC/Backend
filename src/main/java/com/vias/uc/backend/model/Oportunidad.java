package com.vias.uc.backend.model;

import com.vias.uc.backend.model.enums.EstadoOportunidad;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "oportunidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oportunidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oportunidad")
    private Integer idOportunidad;
    
    @Column(name = "id_creador", nullable = false)
    private Integer idCreador;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_creador",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(name = "fk_oportunidades_usuarios")
    )
    private Usuario creador;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", foreignKey = @ForeignKey(name = "fk_oportunidades_empresas"))
    private Empresa empresa;
    
    @Column(name = "titulo")
    private String titulo;
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "requisitos", columnDefinition = "TEXT")
    private String requisitos;
    
    @Column(name = "ubicacion")
    private String ubicacion;
    
    @Column(name = "modalidad")
    private String modalidad;
    
    @Column(name = "tipo")
    private String tipo;
    
    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;
    
    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    // 'activo','borrador','pausada','cerrado'
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoOportunidad estado;


    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    // ===== Getters/Setters =====
    public Integer getIdOportunidad() { return idOportunidad; }
    public void setIdOportunidad(Integer idOportunidad) { this.idOportunidad = idOportunidad; }

    public Integer getIdCreador() { return idCreador; }
    public void setIdCreador(Integer idCreador) { this.idCreador = idCreador; }

    public Usuario getCreador() { return creador; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public LocalDateTime getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(LocalDateTime fechaCierre) { this.fechaCierre = fechaCierre; }

    public EstadoOportunidad getEstado() { return estado; }
    public void setEstado(EstadoOportunidad estado) { this.estado = estado; }

    public Integer getIdAuditoria() { return idAuditoria; }
    public void setIdAuditoria(Integer idAuditoria) { this.idAuditoria = idAuditoria; }
}
