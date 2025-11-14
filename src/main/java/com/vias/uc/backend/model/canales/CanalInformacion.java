package com.vias.uc.backend.model.canales;
import com.vias.uc.backend.model.enums.TipoCanal;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Table(name = "canales_informacion")
public class CanalInformacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_canal")
    private Integer idCanal;

    private String nombre;

    private String slug;

    private String descripcion;

    private boolean activo = true;

    @Column(name = "id_auditoria")
    private Integer idAuditoria;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void setIdAuditoria(Integer idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public Integer getIdAuditoria() {
        return idAuditoria;
    }

    public Integer getId() {
        return idCanal;
    }

    public void setId(Integer id) {
        this.idCanal = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoCanal tipo;

    public TipoCanal getTipo() {
        return tipo;
    }

    public void setTipo(TipoCanal tipo) {
        this.tipo = tipo;
    }

}
