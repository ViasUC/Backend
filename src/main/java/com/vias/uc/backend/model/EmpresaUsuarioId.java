package com.vias.uc.backend.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase de clave primaria compuesta para EmpresaUsuario
 * Combina id_empresa e id_usuario
 */
public class EmpresaUsuarioId implements Serializable {

    private Integer empresa;  // Debe coincidir con el nombre del atributo en EmpresaUsuario
    private Long usuario;  // Debe coincidir con BIGINT en BD y Long en EmpresaUsuario

    public EmpresaUsuarioId() {
    }

    public EmpresaUsuarioId(Integer empresa, Long usuario) {
        this.empresa = empresa;
        this.usuario = usuario;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmpresaUsuarioId that = (EmpresaUsuarioId) o;
        return Objects.equals(empresa, that.empresa) && Objects.equals(usuario, that.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empresa, usuario);
    }
}
