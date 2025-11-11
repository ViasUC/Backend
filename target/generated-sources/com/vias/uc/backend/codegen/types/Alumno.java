package com.vias.uc.backend.codegen.types;

import jakarta.annotation.Generated;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Objects;

@Generated("com.netflix.graphql.dgs.codegen.CodeGen")
@com.vias.uc.backend.codegen.Generated
public class Alumno {
  private String idUsuario;

  private String carrera;

  private int semestre;

  private int idAuditoria;

  private Usuario usuario;

  public Alumno() {
  }

  public String getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(String idUsuario) {
    this.idUsuario = idUsuario;
  }

  public String getCarrera() {
    return carrera;
  }

  public void setCarrera(String carrera) {
    this.carrera = carrera;
  }

  public int getSemestre() {
    return semestre;
  }

  public void setSemestre(int semestre) {
    this.semestre = semestre;
  }

  public int getIdAuditoria() {
    return idAuditoria;
  }

  public void setIdAuditoria(int idAuditoria) {
    this.idAuditoria = idAuditoria;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  @Override
  public String toString() {
    return "Alumno{idUsuario='" + idUsuario + "', carrera='" + carrera + "', semestre='" + semestre + "', idAuditoria='" + idAuditoria + "', usuario='" + usuario + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Alumno that = (Alumno) o;
    return Objects.equals(idUsuario, that.idUsuario) &&
        Objects.equals(carrera, that.carrera) &&
        semestre == that.semestre &&
        idAuditoria == that.idAuditoria &&
        Objects.equals(usuario, that.usuario);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUsuario, carrera, semestre, idAuditoria, usuario);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @com.vias.uc.backend.codegen.Generated
  public static class Builder {
    private String idUsuario;

    private String carrera;

    private int semestre;

    private int idAuditoria;

    private Usuario usuario;

    public Alumno build() {
      Alumno result = new Alumno();
      result.idUsuario = this.idUsuario;
      result.carrera = this.carrera;
      result.semestre = this.semestre;
      result.idAuditoria = this.idAuditoria;
      result.usuario = this.usuario;
      return result;
    }

    public Builder idUsuario(String idUsuario) {
      this.idUsuario = idUsuario;
      return this;
    }

    public Builder carrera(String carrera) {
      this.carrera = carrera;
      return this;
    }

    public Builder semestre(int semestre) {
      this.semestre = semestre;
      return this;
    }

    public Builder idAuditoria(int idAuditoria) {
      this.idAuditoria = idAuditoria;
      return this;
    }

    public Builder usuario(Usuario usuario) {
      this.usuario = usuario;
      return this;
    }
  }
}
