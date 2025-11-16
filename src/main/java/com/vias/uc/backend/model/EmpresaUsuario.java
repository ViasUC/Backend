package com.vias.uc.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que mapea la tabla intermedia 'empresa_usuario'
 * Relaciona usuarios con empresas
 * Usa clave primaria compuesta (id_empresa, id_usuario)
 */
@Entity
@Table(name = "empresa_usuario")
@IdClass(EmpresaUsuarioId.class)
public class EmpresaUsuario {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", nullable = false)
    private Empresa empresa;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "rol_en_empresa", nullable = false, length = 50)
    private String rolEnEmpresa;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_alta", nullable = false)
    private LocalDateTime fechaAlta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_auditoria", nullable = false)
    private Auditoria auditoria;

    // Constructores
    public EmpresaUsuario() {
        this.fechaAlta = LocalDateTime.now();
    }

    // Getters y Setters
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getRolEnEmpresa() {
        return rolEnEmpresa;
    }

    public void setRolEnEmpresa(String rolEnEmpresa) {
        this.rolEnEmpresa = rolEnEmpresa;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }
}
