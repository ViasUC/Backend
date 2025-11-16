package com.vias.uc.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "postulaciones", schema = "public")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion")
    private Integer idPostulacion;

    @Column(name = "id_oportunidad")
    private Integer idOportunidad;

    @Column(name = "id_postulante")
    private Integer idPostulante;

    @Column(name = "id_ofertante")
    private Integer idOfertante;

    @Column(name = "fecha_postulacion")
    private LocalDateTime fechaPostulacion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "motivo")
    private String motivo;

    @Column(name = "id_auditoria")
    private Integer idAuditoria;
}
