# Backend Unificado

Readme para el backend que todos van a usar en sus frontend

## Uso para usuarios de windows:
-Instalar Linux

## Uso para usuarios de Linux :
1-Tener instalado el Java 21.0.8

2-Tener instalado y corriendo el PostgreSQL con las tablas creadas con el .txt que mandó Hector en el grupo de Whatsapp de SE

3-Meter crendeciales de la base de datos local en application.yaml (src/main/resources/application.yml)

4-Sincronizar el maven. Apretar el ciclo de flechas: 
![img.png](img.png)
O correr el siguiente comando en la raíz del proyecto:
```bash
choco upgrade maven
```

5-Correr el programa en BackendApplication.Java

6- Si te sale el error:
![img_1.png](img_1.png)
Ir a src->main->resources y cambiar el nombre de la carpeta graphql por graphql-client

7-Ir a http://localhost:8080/graphiql?path=/graphql (capaz tarda un poquito al cargar)


# Guía (maso) para Alumnos :
Dentro del graphql, así se hacen las siguientes llamadas:

Para crear un Alumno:
```graphql
mutation {
  registrarAlumno(
    input: {
      usuario: {
        nombre: "Matías"
        apellido: "Rojas"
        email: "matias@uc.edu.py"
        telefono: "0999999999"
        ubicacion: "Asunción"
        password: "1234"
      }
      carrera: "Ingeniería Informática"
      semestre: 8
    }
  ) {
    idUsuario
    carrera
    semestre
    usuario {
      nombre
      apellido
      email
    }
  }
}
```

Para hacer login con un Alumno:
```graphql
mutation {
  login(input: {
    email: "matias@uc.edu.py",
    password: "1234"
  }) {
    idUsuario
    nombre
    apellido
    rolPrincipal
  }
}
```


Para que el Alumno cambie sus datos dentro de su perfil:
```graphql
mutation {
  actualizarAlumno(id: 2, input: {
    usuario: {
      nombre: "Rogesadfsadfr",
      apellido: "Gonzfffsdf",
      email: "matias@uc.es.py"
      ubicacion: "Asuncionsadfasdf"
      telefono: "09848599333sdfsd"
    },
    carrera: "Ingeniería Infasdfsdformática",
    semestre: 70

  }) {
    usuario { nombre apellido email }
    carrera
    semestre
  }
}
```