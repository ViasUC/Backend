package com.vias.uc.backend.model;

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
    
    @Column(name = "estado")
    private String estado;
    
    @Column(name = "id_auditoria")
    private Integer idAuditoria;
}
