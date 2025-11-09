package com.vias.uc.backend.codegen.types;

import jakarta.annotation.Generated;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Objects;

@Generated("com.netflix.graphql.dgs.codegen.CodeGen")
@com.vias.uc.backend.codegen.Generated
public class Usuario {
  private String idUsuario;

  private String nombre;

  private String apellido;

  private String email;

  private String telefono;

  private String ubicacion;

  private RolUsuario rolPrincipal;

  private Integer completitud;

  public Usuario() {
  }

  public String getIdUsuario() {
    return idUsuario;
  }

  public void setIdUsuario(String idUsuario) {
    this.idUsuario = idUsuario;
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
    return "Usuario{idUsuario='" + idUsuario + "', nombre='" + nombre + "', apellido='" + apellido + "', email='" + email + "', telefono='" + telefono + "', ubicacion='" + ubicacion + "', rolPrincipal='" + rolPrincipal + "', completitud='" + completitud + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Usuario that = (Usuario) o;
    return Objects.equals(idUsuario, that.idUsuario) &&
        Objects.equals(nombre, that.nombre) &&
        Objects.equals(apellido, that.apellido) &&
        Objects.equals(email, that.email) &&
        Objects.equals(telefono, that.telefono) &&
        Objects.equals(ubicacion, that.ubicacion) &&
        Objects.equals(rolPrincipal, that.rolPrincipal) &&
        Objects.equals(completitud, that.completitud);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUsuario, nombre, apellido, email, telefono, ubicacion, rolPrincipal, completitud);
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  @Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @com.vias.uc.backend.codegen.Generated
  public static class Builder {
    private String idUsuario;

    private String nombre;

    private String apellido;

    private String email;

    private String telefono;

    private String ubicacion;

    private RolUsuario rolPrincipal;

    private Integer completitud;

    public Usuario build() {
      Usuario result = new Usuario();
      result.idUsuario = this.idUsuario;
      result.nombre = this.nombre;
      result.apellido = this.apellido;
      result.email = this.email;
      result.telefono = this.telefono;
      result.ubicacion = this.ubicacion;
      result.rolPrincipal = this.rolPrincipal;
      result.completitud = this.completitud;
      return result;
    }

    public Builder idUsuario(String idUsuario) {
      this.idUsuario = idUsuario;
      return this;
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
