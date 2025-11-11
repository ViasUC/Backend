package com.vias.uc.backend.codegen.types;

import jakarta.annotation.Generated;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Objects;

@Generated("com.netflix.graphql.dgs.codegen.CodeGen")
@com.vias.uc.backend.codegen.Generated
public class RegistroAlumnoInput {
  private UsuarioRegistroInput usuario;

  private String carrera;

  private int semestre;

  private String detalleAuditoria = "registro alumno";

  public RegistroAlumnoInput() {
  }

  public UsuarioRegistroInput getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioRegistroInput usuario) {
    this.usuario = usuario;
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

  public String getDetalleAuditoria() {
    return detalleAuditoria;
  }

  public void setDetalleAuditoria(String detalleAuditoria) {
    this.detalleAuditoria = detalleAuditoria;
  }

  @Override
  public String toString() {
    return "RegistroAlumnoInput{usuario='" + usuario + "', carrera='" + carrera + "', semestre='" + semestre + "', detalleAuditoria='" + detalleAuditoria + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RegistroAlumnoInput that = (RegistroAlumnoInput) o;
    return Objects.equals(usuario, that.usuario) &&
        Objects.equals(carrera, that.carrera) &&
        semestre == that.semestre &&
        Objects.equals(detalleAuditoria, that.detalleAuditoria);
  }

  @Override
  public int hashCode() {
    return Objects.hash(usuario, carrera, semestre, detalleAuditoria);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @com.vias.uc.backend.codegen.Generated
  public static class Builder {
    private UsuarioRegistroInput usuario;

    private String carrera;

    private int semestre;

    private String detalleAuditoria = "registro alumno";

    public RegistroAlumnoInput build() {
      RegistroAlumnoInput result = new RegistroAlumnoInput();
      result.usuario = this.usuario;
      result.carrera = this.carrera;
      result.semestre = this.semestre;
      result.detalleAuditoria = this.detalleAuditoria;
      return result;
    }

    public Builder usuario(UsuarioRegistroInput usuario) {
      this.usuario = usuario;
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

    public Builder detalleAuditoria(String detalleAuditoria) {
      this.detalleAuditoria = detalleAuditoria;
      return this;
    }
  }
}
