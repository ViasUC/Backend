package com.vias.uc.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "oportunidades", schema = "public")
public class Oportunidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oportunidad")
    private Integer idOportunidad;

    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(name = "id_creador")
    private Integer idCreador;

    private String titulo;
    private String descripcion;
    private String requisitos;
    private String ubicacion;
    private String modalidad;
    private String tipo;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;

    private String estado;

    //  👇🔥 ESTE FALTABA
    @Column(name = "id_auditoria", nullable = false)
    private Integer idAuditoria;
}
