package com.vias.uc.backend.codegen.types;

import jakarta.annotation.Generated;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Objects;

@Generated("com.netflix.graphql.dgs.codegen.CodeGen")
@com.vias.uc.backend.codegen.Generated
public class AlumnoInput {
  private UsuarioInput usuario;

  private String carrera;

  private Integer semestre;

  public AlumnoInput() {
  }

  public UsuarioInput getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioInput usuario) {
    this.usuario = usuario;
  }

  public String getCarrera() {
    return carrera;
  }

  public void setCarrera(String carrera) {
    this.carrera = carrera;
  }

  public Integer getSemestre() {
    return semestre;
  }

  public void setSemestre(Integer semestre) {
    this.semestre = semestre;
  }

  @Override
  public String toString() {
    return "AlumnoInput{usuario='" + usuario + "', carrera='" + carrera + "', semestre='" + semestre + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AlumnoInput that = (AlumnoInput) o;
    return Objects.equals(usuario, that.usuario) &&
        Objects.equals(carrera, that.carrera) &&
        Objects.equals(semestre, that.semestre);
  }

  @Override
  public int hashCode() {
    return Objects.hash(usuario, carrera, semestre);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @com.vias.uc.backend.codegen.Generated
  public static class Builder {
    private UsuarioInput usuario;

    private String carrera;

    private Integer semestre;

    public AlumnoInput build() {
      AlumnoInput result = new AlumnoInput();
      result.usuario = this.usuario;
      result.carrera = this.carrera;
      result.semestre = this.semestre;
      return result;
    }

    public Builder usuario(UsuarioInput usuario) {
      this.usuario = usuario;
      return this;
    }

    public Builder carrera(String carrera) {
      this.carrera = carrera;
      return this;
    }

    public Builder semestre(Integer semestre) {
      this.semestre = semestre;
      return this;
    }
  }
}
