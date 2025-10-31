package com.vias.uc.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios",
        uniqueConstraints = @UniqueConstraint(name = "uk_usuarios_email", columnNames = "email"))
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false)
    private String nombre;

    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "rol_principal")
    private String rolPrincipal;

    @Column(name = "completitud", nullable = false)
    private Integer completitud = 0;

    // Relación obligatoria con Auditoria (id_auditoria es NOT NULL en BD)
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_auditoria", nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuarios_auditoria"))
    private Auditoria auditoria;

    // ======= getters / setters =======

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRolPrincipal() {
        return rolPrincipal;
    }

    public void setRolPrincipal(String rolPrincipal) {
        this.rolPrincipal = rolPrincipal;
    }

    public Integer getCompletitud() {
        return completitud;
    }

    public void setCompletitud(Integer completitud) {
        this.completitud = completitud;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

    // ======= constructores =======

    public Usuario() { }

    public Usuario(String nombre, String apellido, String email, String rolPrincipal, Integer completitud, Auditoria auditoria) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rolPrincipal = rolPrincipal;
        this.completitud = (completitud != null ? completitud : 0);
        this.auditoria = auditoria;
    }
}
