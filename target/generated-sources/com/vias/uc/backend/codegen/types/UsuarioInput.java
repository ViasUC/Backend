package com.vias.uc.backend.codegen.types;

import jakarta.annotation.Generated;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.Objects;

@Generated("com.netflix.graphql.dgs.codegen.CodeGen")
@com.vias.uc.backend.codegen.Generated
public class UsuarioInput {
  private String nombre;

  private String apellido;

  private String email;

  private String telefono;

  private String ubicacion;

  public UsuarioInput() {
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

  @Override
  public String toString() {
    return "UsuarioInput{nombre='" + nombre + "', apellido='" + apellido + "', email='" + email + "', telefono='" + telefono + "', ubicacion='" + ubicacion + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UsuarioInput that = (UsuarioInput) o;
    return Objects.equals(nombre, that.nombre) &&
        Objects.equals(apellido, that.apellido) &&
        Objects.equals(email, that.email) &&
        Objects.equals(telefono, that.telefono) &&
        Objects.equals(ubicacion, that.ubicacion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre, apellido, email, telefono, ubicacion);
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

    public UsuarioInput build() {
      UsuarioInput result = new UsuarioInput();
      result.nombre = this.nombre;
      result.apellido = this.apellido;
      result.email = this.email;
      result.telefono = this.telefono;
      result.ubicacion = this.ubicacion;
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
  }
}
