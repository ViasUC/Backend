package com.vias.uc.backend.codegen;

import java.lang.String;

@jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
@Generated
public class DgsConstants {
  public static final String QUERY_TYPE = "Query";

  public static final String MUTATION_TYPE = "Mutation";

  @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @Generated
  public static class USUARIO {
    public static final String TYPE_NAME = "Usuario";

    public static final String IdUsuario = "idUsuario";

    public static final String Nombre = "nombre";

    public static final String Apellido = "apellido";

    public static final String Email = "email";

    public static final String Telefono = "telefono";

    public static final String Ubicacion = "ubicacion";

    public static final String RolPrincipal = "rolPrincipal";

    public static final String Completitud = "completitud";
  }

  @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @Generated
  public static class ALUMNO {
    public static final String TYPE_NAME = "Alumno";

    public static final String IdUsuario = "idUsuario";

    public static final String Carrera = "carrera";

    public static final String Semestre = "semestre";

    public static final String IdAuditoria = "idAuditoria";

    public static final String Usuario = "usuario";
  }

  @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @Generated
  public static class QUERY {
    public static final String TYPE_NAME = "Query";

    public static final String Alumnos = "alumnos";

    public static final String Alumno = "alumno";

    public static final String AlumnoPorEmail = "alumnoPorEmail";

    @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
    @Generated
    public static class ALUMNO_INPUT_ARGUMENT {
      public static final String Id = "id";
    }

    @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
    @Generated
    public static class ALUMNOPOREMAIL_INPUT_ARGUMENT {
      public static final String Email = "email";
    }
  }

  @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @Generated
  public static class MUTATION {
    public static final String TYPE_NAME = "Mutation";

    public static final String RegistrarAlumno = "registrarAlumno";

    public static final String ActualizarAlumno = "actualizarAlumno";

    @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
    @Generated
    public static class REGISTRARALUMNO_INPUT_ARGUMENT {
      public static final String Input = "input";
    }

    @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
    @Generated
    public static class ACTUALIZARALUMNO_INPUT_ARGUMENT {
      public static final String Id = "id";

      public static final String Input = "input";
    }
  }

  @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @Generated
  public static class USUARIOINPUT {
    public static final String TYPE_NAME = "UsuarioInput";

    public static final String Nombre = "nombre";

    public static final String Apellido = "apellido";

    public static final String Email = "email";

    public static final String Telefono = "telefono";

    public static final String Ubicacion = "ubicacion";
  }

  @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @Generated
  public static class ALUMNOINPUT {
    public static final String TYPE_NAME = "AlumnoInput";

    public static final String Usuario = "usuario";

    public static final String Carrera = "carrera";

    public static final String Semestre = "semestre";
  }

  @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @Generated
  public static class USUARIOREGISTROINPUT {
    public static final String TYPE_NAME = "UsuarioRegistroInput";

    public static final String Nombre = "nombre";

    public static final String Apellido = "apellido";

    public static final String Email = "email";

    public static final String Telefono = "telefono";

    public static final String Ubicacion = "ubicacion";

    public static final String Password = "password";

    public static final String RolPrincipal = "rolPrincipal";

    public static final String Completitud = "completitud";
  }

  @jakarta.annotation.Generated("com.netflix.graphql.dgs.codegen.CodeGen")
  @Generated
  public static class REGISTROALUMNOINPUT {
    public static final String TYPE_NAME = "RegistroAlumnoInput";

    public static final String Usuario = "usuario";

    public static final String Carrera = "carrera";

    public static final String Semestre = "semestre";

    public static final String DetalleAuditoria = "detalleAuditoria";
  }
}
