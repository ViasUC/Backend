package com.vias.uc.backend.model;

import com.vias.uc.backend.model.enums.RolUsuario;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios",
        uniqueConstraints = @UniqueConstraint(name = "uk_usuarios_email", columnNames = "email"))
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false)
    private String nombre;

    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    // Campo password requerido por la BD (NOT NULL)
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol_principal", columnDefinition = "rol_usuario", nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private RolUsuario rolPrincipal;

    @Column(name = "completitud", nullable = false)
    private Integer completitud = 0;

    // Relación obligatoria con Auditoria (id_auditoria es NOT NULL en BD)
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_auditoria", nullable = false,
            foreignKey = @ForeignKey(name = "fk_usuarios_auditoria"))
    private Auditoria auditoria;

    // ======= constructores =======

    public Usuario() { }

    public Usuario(String nombre,
                   String apellido,
                   String email,
                   String password,
                   RolUsuario rolPrincipal,
                   Integer completitud,
                   Auditoria auditoria) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rolPrincipal = rolPrincipal;
        this.completitud = (completitud != null ? completitud : 0);
        this.auditoria = auditoria;
    }

    // ======= getters / setters =======

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RolUsuario getRolPrincipal() {
        return rolPrincipal;
    }

    public void setRolPrincipal(RolUsuario rolPrincipal) {
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
}
