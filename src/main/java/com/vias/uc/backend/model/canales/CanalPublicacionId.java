package com.vias.uc.backend.model.canales;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CanalPublicacionId implements Serializable {

    @Column(name = "id_canal")
    private Integer idCanal;

    @Column(name = "id_publicacion")
    private Integer idPublicacion;

    public CanalPublicacionId() {
    }

    public CanalPublicacionId(Integer idCanal, Integer idPublicacion) {
        this.idCanal = idCanal;
        this.idPublicacion = idPublicacion;
    }

    public Integer getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(Integer idCanal) {
        this.idCanal = idCanal;
    }

    public Integer getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(Integer idPublicacion) {
        this.idPublicacion = idPublicacion;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CanalPublicacionId)) return false;
        CanalPublicacionId that = (CanalPublicacionId) o;
        return Objects.equals(idCanal, that.idCanal)
                && Objects.equals(idPublicacion, that.idPublicacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCanal, idPublicacion);
    }
}
