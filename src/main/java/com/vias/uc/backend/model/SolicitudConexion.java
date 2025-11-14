package com.vias.uc.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "solicitudes_conexion")
public class SolicitudConexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @Column(name = "id_usuario_origen")
    private Integer idUsuarioOrigen;

    @Column(name = "id_usuario_destino")
    private Integer idUsuarioDestino;

    @Column(name = "estado")
    private String estado; // pendiente | aceptada | rechazada

    @Column(name = "fecha_solicitud")
    private LocalDate fechaSolicitud;

    @Column(name = "fecha_respuesta")
    private LocalDate fechaRespuesta;

    @Column(name = "id_auditoria")
    private Integer idAuditoria;
}
