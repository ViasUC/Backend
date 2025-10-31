package com.vias.uc.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alumnos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alumno {

    // La PK es id_usuario (FK a usuarios). Usamos @MapsId para compartir el ID.
    @Id
    @Column(name = "id_usuario")
    private Long idUsuario;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "carrera", nullable = false)
    private String carrera;

    @Column(name = "semestre", nullable = false)
    private Integer semestre;

    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_auditoria", nullable = false)
    private Auditoria auditoria;
}
