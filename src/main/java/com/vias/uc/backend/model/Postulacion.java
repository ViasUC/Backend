package com.vias.uc.backend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "postulaciones")
@Getter
@Setter
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_postulacion")
    private Long idPostulacion;

    @Column(name = "fecha_postulacion", nullable = false)
    private LocalDateTime fechaPostulacion = LocalDateTime.now();

    @Column(name = "mensaje")
    private String mensaje;

    @Column(name = "curriculum_url")
    private String curriculumUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPostulacion estado = EstadoPostulacion.PENDIENTE;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oportunidad", nullable = false)
    private Oportunidad oportunidad;

    public enum EstadoPostulacion {
        PENDIENTE,
        ACEPTADA,
        RECHAZADA
    }
}
