package com.vias.uc.backend.model.canales;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CanalSeguidorId implements Serializable {

    @Column(name = "id_canal")
    private Integer idCanal;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    public CanalSeguidorId() {
    }

    public CanalSeguidorId(Integer idCanal, Integer idUsuario) {
        this.idCanal = idCanal;
        this.idUsuario = idUsuario;
    }

    public Integer getIdCanal() {
        return idCanal;
    }

    public void setIdCanal(Integer idCanal) {
        this.idCanal = idCanal;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CanalSeguidorId that = (CanalSeguidorId) o;
        return Objects.equals(idUsuario, that.idUsuario) &&
                Objects.equals(idCanal, that.idCanal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, idCanal);
    }

}
