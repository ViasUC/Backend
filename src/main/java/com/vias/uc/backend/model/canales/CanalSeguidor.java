package com.vias.uc.backend.model.canales;

import com.vias.uc.backend.model.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "canales_seguidores")
public class CanalSeguidor {

    @EmbeddedId
    private CanalSeguidorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idCanal")
    @JoinColumn(name = "id_canal")
    private CanalInformacion canal;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "fecha_alta")
    private LocalDateTime fechaAlta = LocalDateTime.now();

    private boolean activo = true;

    @Column(name = "id_auditoria")
    private Long idAuditoria;

    public CanalSeguidor() {
    }

    public CanalSeguidor(Usuario usuario, CanalInformacion canal) {
        this.usuario = usuario;
        this.canal = canal;
        this.id = new CanalSeguidorId(usuario.getIdUsuario(), canal.getId());
        this.fechaAlta= LocalDateTime.now();
    }

    public CanalSeguidorId getId() {
        return id;
    }

    public void setId(CanalSeguidorId id) {
        this.id = id;
    }

    public CanalInformacion getCanal() {
        return canal;
    }

    public void setCanal(CanalInformacion canal) {
        this.canal = canal;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Long getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(Long idAuditoria) {
        this.idAuditoria = idAuditoria;
    }
}
