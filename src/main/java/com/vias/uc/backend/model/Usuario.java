package com.vias.uc.backend.model;

import com.vias.uc.backend.model.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name = "usuarios", schema = "public")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false)
    private String nombre;

    private String apellido;
    private String ubicacion;
    private String telefono;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Integer completitud;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)                // Hibernate 6: usa el tipo ENUM nativo de PG
    @Column(name = "rol_principal", columnDefinition = "rol_usuario")
    private RolUsuario rolPrincipal;

    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    @Column(name = "email_verificado")
    private Boolean emailVerificado;

    @Column(name = "token_verificacion")
    private String tokenVerificacion;

    @Column(name = "token_verificacion_expira")
    private LocalDateTime tokenVerificacionExpira;

}
