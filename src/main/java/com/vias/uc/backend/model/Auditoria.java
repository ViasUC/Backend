package com.vias.uc.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    @Column(name = "actor_id")
    private Integer actorId; // Cambiá a Long si tus IDs de usuario son Long

    @Column(name = "accion")
    private String accion;

    @Column(name = "detalle")
    private String detalle;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;

    @PrePersist
    public void prePersist() {
        if (fechaEvento == null) fechaEvento = LocalDateTime.now();
    }
}
