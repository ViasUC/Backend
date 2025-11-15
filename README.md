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
ara crear un Alumno:
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
# DOCENTES

## F0: Gestión de perfiles

### Registro al sistema: Docente
```graphql
mutation registrarProfesor{
  registrarProfesor(
    input: {
      usuario: {
        nombre: "Lucía"
        apellido: "González"
        email: "lucia@uca.edu.py"
        telefono: "0981999999"
        ubicacion: "Asunción"
        password: "1234"
      }
      departamento: "Informática"
      categoriaDocente: "Titular"
      areasDocentes: "SE1, Arquitectura"
    }
  ) {
    idUsuario
    departamento
    categoriaDocente
    usuario {
      nombre
      email
    }
  }
}
```

### Registro al sistema: Investigador
```graphql
mutation registrarInvestigador{
  registrarInvestigador(
    input: {
      usuario: {
        nombre: "Diego"
        apellido: "Medina"
        email: "diego@uca.edu.py"
        telefono: "0981222333"
        ubicacion: "Encarnación"
        password: "abcd"
      }
      areasInvestigacion: "Inteligencia Artificial, Deep Learning"
      afiliaciones: "UCA, Laboratorio de IA"
      hindex: 5
    }
  ) {
    idUsuario
    areasInvestigacion
    afiliaciones
    hindex
    usuario {
      nombre
      email
      rolPrincipal
    }
  }
}
```

### Inicio de sesión: Docente/Investigador
```graphql
mutation loginGeneral{
  login(input: {
    email: "luciana@uca.edu.py",
    password: "luci123"
  }) {
    idUsuario
    nombre
    apellido
    rolPrincipal
  }
}
```

### Actualizar Perfil: Docente
```graphql
mutation actualizarProfesor{
  actualizarProfesor(id: 1020, input: {
    usuario: { nombre: "Luciana", apellido: "González" email: "luciana@uca.edu.py", password: "luci123" }
    departamento: "Software"
    categoriaDocente: "Actualizada"
  }) {
    usuario { nombre email }
    departamento
  }
}
```

### Actualizar Perfil: Investigador
```graphql
mutation actualizarInvestigador{
  actualizarInvestigador(
    id: 1021
    input: {
      usuario: {
        nombre: "Diego M."
        email: "diego@uca.edu.py"
        # opcional para cambiar clave (se hashea con bcrypt):
        #password: "nueva123"
        ubicacion: "Luque"
      }
      areasInvestigacion: "ChatGPT"
      afiliaciones: "Lab IA"
      hindex: 8
    }
  ) {
    idUsuario
    areasInvestigacion
    afiliaciones
    hindex
    usuario { nombre email }
  }
}

```

## F1: Manejo de postulantes + Ejemplos de uso en GraphQL

### Crear Postulación (ALUMNO)
```graphql
mutation crearPostulacion {
  crearPostulacion(idAlumno: 47, idOportunidad: 4) {
    idPostulacion
    estado
    alumno { usuario { nombre } }
    oportunidad { titulo }
    postulante { nombre } 
  }
}
```
### Listar Postulación
```graphql
query listarPostulacion{
  postulacionesPage(
    filtro: {
      idOportunidad: 5
      estados: [PENDIENTE, ACEPTADA, CANCELADA]
    }
    page: 0
    size: 10
  ) {
    total
    items {
      idPostulacion
      estado
      fechaPostulacion
      postulante { nombre }
      oportunidad { titulo }
    }
  }
}
```

### Actualizar Estado de Postulación
```graphql
mutation actualizarEstadoPostulacion {
  actualizarEstadoPostulacion(
    idPostulacion: 44,
    estado: CANCELADA,
    motivo: "Motivo desconocido"
    idActor: 1024
  ) {
    idPostulacion
    estado
  }
}
```
**Transiciones válidas para update endpoints:**
- PENDIENTE	→ ACEPTADA  : Cuando el postulante es seleccionado.
- PENDIENTE	→ RECHAZADA : Cuando no cumple con los requisitos.
- PENDIENTE	→ CANCELADA : Cuando el postulante retira su solicitud o se cierra el proceso.
- ACEPTADA	→ CANCELADA : Si por alguna razón se revoca la aceptación.
- RECHAZADA	→ CANCELADA : Si el registro se anula o la postulación se borra administrativamente.
- CANCELADA	-	No puede cambiar más.

### Consultar Historial de Postulación
```graphql
query historialPostulacion {
  historialPostulacion(idPostulacion: 20) {
    fechaCambio
    estadoAnterior
    estadoNuevo
    motivo
  }
}
```

### Listar Oportunidades
```graphql
query listarOportunidades {
  oportunidades {
    idOportunidad
    titulo
    estado
    fechaPublicacion
  }
}
```

### Listar Oportunidades por Creador
```graphql
query oportunidadesPorCreador{
  oportunidadesPorCreador(creadorId: 1024) {
    idOportunidad
    titulo
    estado
    creador {
      idUsuario
      apellido
      rolPrincipal
    }
  }
}
```

### Crear Oportunidad
```graphql
mutation crearOportunidadDocente{
  crearOportunidadDocente(input: {
    idCreador: 51
    titulo: "Pasantía QA Backend"
    descripcion: "Testing de APIs GraphQL"
    requisitos: "Java, JUnit, Postman"
    ubicacion: "Asunción"
    modalidad: "híbrido"
    tipo: "pasantía"
    fechaCierre: "2025-12-31T23:59:00"
    estado: "activo"
  }) {
    idOportunidad
    titulo
    estado
    fechaPublicacion
    creador { idUsuario nombre email }
  }
}
```

## F3: Endorsements

### Crear Endorsement
```graphql
mutation createEndorsement{
  createEndorsement(
    input: { fromUserId: 1024, toUserId: 1000, skill: "Mejor alumno INFO 3", message: "Enhorabuena" }
  ) {
    idEndorsement
    status
    fromUserId
    toUserId
    skill
    message
    createdAt
  }
}
```

### Consultar Endorsement (RECEPTOR)
```graphql
query endorsementsPendientes{
  endorsementsReceived(toUserId: 1000, status: PENDING) {
    idEndorsement
    fromUserId
    skill
    message
    status
  }
}
```

### Consultar Endorsement Dados
```graphql
query endorsementsGiven{
  endorsementsGiven(fromUserId: 1024) {
    idEndorsement
    toUserId
    skill
    message
    status
  }
}
```

### Aceptar/Rechazar Endorsement (RECEPTOR)
```graphql
mutation endorsementDecision{
  decideEndorsement(
    input: {
      id: 19
      actorId: 1000
      accept: true
    }
  ) {
    idEndorsement
    status
  }
}
```

## F7: Canales de información

### Canales Activos
```graphql
query CanalesActivos{
  canalesActivos {
    idCanal
    nombre
    slug
    tipo
    descripcion
    activo
  }
}
```

### Crear Canal de Información
```graphql
mutation crearCanal {
  crearCanal(input: {
    nombre: "Prueba"
    slug: "prueba-1"
    tipo: "EVENTOS"
    descripcion: "Descripción de prueba"
    actorId: 1025
  }) {
    idCanal
    nombre
    slug
  }
}
```

### Consultar Publicaciones Asociadas a un Canal (FUNCIÓN GENERAL)
```graphql
query publicacionesDeCanal{
  publicacionesDeCanal(idCanal: 12) {
    idPublicacion
    observacion
    estado
    fechaPublicacion
  }
}
```

### Crear Publicación en un Canal Específico
```graphql
mutation crearPublicacionEnCanal{
  crearPublicacionEnCanal(
    input: {
      idCanal: 12
      idProyectoF7: 1
      idAutor: 1024
      titulo: "Post sobre Innovación y Desarrollo"
      contenido: "Descripción del post sobre Innovación y Desarrollo"
    }
  ) {
    idPublicacion
    titulo
    observacion
    estado
    fechaPublicacion
    autor {
      idUsuario
      nombre
    }
  }
}
```

### Seguir a un Canal
```graphql
mutation seguirCanal{
  seguirCanal(idCanal: 12, idUsuario: 32)
}
```

### Dejar de Seguir a un Canal
```graphql
mutation dejarDeSeguirCanal{
  dejarDeSeguirCanal(idCanal: 12, idUsuario: 32)
}
```

### Destacar una Publicación
```graphql
mutation destacarPublicacion{
  destacarPublicacion(idCanal: 15, idPublicacion: 16, destacado: true)
}
```

### Consultar Canales por Tipo
```graphql
query canalesPorTipo{
  canalesPorTipo(tipo: OFERTAS) {
    idCanal
    nombre
    tipo
  }
}
```

### Consultar Canales Seguidos por Usuario en Específico
```graphql
query canalesSeguidos {
  canalesSeguidos(idUsuario: 32) {
    idCanal
    nombre
    slug
    tipo
  }
}
```

### Consultar Publicaciones Asociadas a Seguimiento de Canales por Usuarios (FEED)
```graphql
query feedCanalesSeguidos{
  feedCanalesSeguidos(idUsuario: 32) {
    idPublicacion
    titulo
    observacion
    estado
    fechaPublicacion
    autor {
      idUsuario
      nombre
      apellido
    }
  }
}
```
