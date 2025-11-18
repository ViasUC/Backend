package com.vias.uc.backend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "endorsements_alumni", schema = "public")
public class Endorsement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endorsement")
    private Integer idEndorsement;

    @Column(name = "id_usuario_emisor", nullable = false)
    private Integer idUsuarioEmisor;

    @Column(name = "id_usuario_receptor", nullable = false)
    private Integer idUsuarioReceptor;

    @Column(name = "fecha_endorsement")
    private LocalDate fechaEndorsement;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "id_auditoria")
    private Integer idAuditoria;
}
