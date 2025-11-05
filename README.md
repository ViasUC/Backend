# Backend Unificado

Readme para el backend que todos van a usar en sus frontend

## Uso para usuarios de windows:
-Instalar Linux

## Uso para usuarios de Linux :
1-Tener instalado el Java 21.0.8

2-Tener instalado y corriendo el PostgreSQL con las tablas creadas

3-Meter crendeciales de la base de datos local en application.yaml

4-Sincronizar el maven. Apretar el ciclo de flechas: 
![img.png](img.png)

5-Correr el programa en BackendApplication.Java

6-Ir a http://localhost:8080/graphiql?path=/graphql 

# DOCENTES

## F1: Manejo de postulantes + Ejemplos de uso en GraphQL

### Crear Postulación
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
query listarPostulacion {
  postulacionesPage(
    filtro: {
      idOportunidad: 4
      estados: [PENDIENTE, ACEPTADA]
      texto: "Roberto"
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
    idPostulacion: 20,
    estado: ACEPTADA,
    motivo: "Cumple los requisitos del puesto"
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

### Listar Alumnos
```graphql
query verAlumnos {
  alumnos {
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

### Crear Alumnos
```graphql
mutation crearAlumno {
  crearAlumno(
    nombre: "Jose"
    apellido: "Pereira"
    email: "jpereira@example.com"
    carrera: "Ingeniería Informática"
    semestre: 6
  ) {
    usuario {
      nombre
      apellido
      email
    }
    carrera
    semestre
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
