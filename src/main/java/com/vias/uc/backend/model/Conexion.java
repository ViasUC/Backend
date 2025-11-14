package com.vias.uc.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import lombok.Data;

@Data

@Entity
@Table(name = "conexiones")
public class Conexion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conexion")
    private Integer idConexion;

    @Column(name = "id_usuario_1")
    private Integer idUsuario1;

    @Column(name = "id_usuario_2")
    private Integer idUsuario2;

    @Column(name = "fecha_conexion")
    private LocalDate fechaConexion;

    @Column(name = "id_auditoria")
    private Integer idAuditoria;
}

