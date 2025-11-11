package com.vias.uc.backend.codegen.types;

import jakarta.annotation.Generated;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Objects;

@Generated("com.netflix.graphql.dgs.codegen.CodeGen")
@com.vias.uc.backend.codegen.Generated
public class UsuarioRegistroInput {
  private String nombre;

  private String apellido;

  private String email;

  private String telefono;

  private String ubicacion;

  private String password;

  private RolUsuario rolPrincipal = RolUsuario.alumno;

  private Integer completitud = 0;

  public UsuarioRegistroInput() {
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

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getUbicacion() {
    return ubicacion;
  }

  public void setUbicacion(String ubicacion) {
    this.ubicacion = ubicacion;
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

  @Override
  public String toString() {
    return "UsuarioRegistroInput{nombre='" + nombre + "', apellido='" + apellido + "', email='" + email + "', telefono='" + telefono + "', ubicacion='" + ubicacion + "', password='" + password + "', rolPrincipal='" + rolPrincipal + "', completitud='" + completitud + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UsuarioRegistroInput that = (UsuarioRegistroInput) o;
    return Objects.equals(nombre, that.nombre) &&
        Objects.equals(apellido, that.apellido) &&
        Objects.equals(email, that.email) &&
        Objects.equals(telefono, that.telefono) &&
        Objects.equals(ubicacion, that.ubicacion) &&
        Objects.equals(password, that.password) &&
        Objects.equals(rolPrincipal, that.rolPrincipal) &&
        Objects.equals(completitud, that.completitud);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre, apellido, email, telefono, ubicacion, password, rolPrincipal, completitud);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @com.vias.uc.backend.codegen.Generated
  public static class Builder {
    private String nombre;

    private String apellido;

    private String email;

    private String telefono;

    private String ubicacion;

    private String password;

    private RolUsuario rolPrincipal = RolUsuario.alumno;

    private Integer completitud = 0;

    public UsuarioRegistroInput build() {
      UsuarioRegistroInput result = new UsuarioRegistroInput();
      result.nombre = this.nombre;
      result.apellido = this.apellido;
      result.email = this.email;
      result.telefono = this.telefono;
      result.ubicacion = this.ubicacion;
      result.password = this.password;
      result.rolPrincipal = this.rolPrincipal;
      result.completitud = this.completitud;
      return result;
    }

    public Builder nombre(String nombre) {
      this.nombre = nombre;
      return this;
    }

    public Builder apellido(String apellido) {
      this.apellido = apellido;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder telefono(String telefono) {
      this.telefono = telefono;
      return this;
    }

    public Builder ubicacion(String ubicacion) {
      this.ubicacion = ubicacion;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder rolPrincipal(RolUsuario rolPrincipal) {
      this.rolPrincipal = rolPrincipal;
      return this;
    }

    public Builder completitud(Integer completitud) {
      this.completitud = completitud;
      return this;
    }
  }
}
