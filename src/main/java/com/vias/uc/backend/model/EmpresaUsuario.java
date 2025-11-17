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
    @Column(name = "id_empresa", nullable = false)
    private Integer empresa;

    @Id
    @Column(name = "id_usuario", nullable = false)
    private Long usuario;

    // Relaciones para acceso a las entidades completas (no parte de la PK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa", insertable = false, updatable = false)
    private Empresa empresaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuarioEntity;

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

    // Getters y Setters para IDs (parte de la PK)
    public Integer getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Integer empresa) {
        this.empresa = empresa;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    // Getters y Setters para las entidades completas
    public Empresa getEmpresaEntity() {
        return empresaEntity;
    }

    public void setEmpresaEntity(Empresa empresaEntity) {
        this.empresaEntity = empresaEntity;
    }

    public Usuario getUsuarioEntity() {
        return usuarioEntity;
    }

    public void setUsuarioEntity(Usuario usuarioEntity) {
        this.usuarioEntity = usuarioEntity;
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
