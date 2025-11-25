package com.vias.uc.backend.model.canales;

import com.vias.uc.backend.model.Publicacion;
import jakarta.persistence.*;

@Entity
@Table(name = "canales_publicaciones")
public class CanalPublicacion {

    @EmbeddedId
    private CanalPublicacionId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idCanal")
    @JoinColumn(name = "id_canal")
    private CanalInformacion canal;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idPublicacion")
    @JoinColumn(name = "id_publicacion", insertable = false, updatable = false)
    private Publicacion publicacion;

    @Column(name = "destacado")
    private boolean destacado;

    public CanalPublicacionId getId() {
        return id;
    }

    public void setId(CanalPublicacionId id) {
        this.id = id;
    }

    public CanalInformacion getCanal() {
        return canal;
    }

    public void setCanal(CanalInformacion canal) {
        this.canal = canal;
    }

    public Publicacion getPublicacion() {
        return publicacion;
    }

    public void setPublicacion(Publicacion publicacion) {
        this.publicacion = publicacion;
    }

    public boolean isDestacado() {
        return destacado;
    }

    public void setDestacado(boolean destacado) {
        this.destacado = destacado;
    }
}
