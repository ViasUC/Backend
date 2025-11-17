package com.vias.uc.backend.model;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "portafolio", schema = "public")
@Getter @Setter
public class Portafolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_portafolio")
    private Integer idPortafolio; // PK única de esta tabla

    // FK única a usuarios.id_usuario (1–a–1)
    @OneToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "id_usuario", insertable = false, updatable = false)
    private Integer idUsuario;
    // Si además querés leer el id sin cargar Usuario:
    // @Column(name = "id_usuario", insertable = false, updatable = false)
    // private Long idUsuario;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "skills")
    private String skills;

    // elegí: Boolean ó un ENUM/STRING. Si en BD guardás 'privado'/'publico', usa String o Enum.
    @Column(name = "visibilidad")
    private boolean visibilidad; // o Boolean si la columna es booleana

    // Usá el mismo tipo que en la columna (timestamp sin tz -> LocalDateTime)
    @Column(name = "ultima_actualizacion")
    private java.time.LocalDateTime ultimaActualizacion;

    // Auditoría: en tus otras tablas la estás usando como BIGINT. Alineá tipos.
    @Column(name = "id_auditoria")
    private Long idAuditoria;
}
