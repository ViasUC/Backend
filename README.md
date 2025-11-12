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

### Inicio de sesión: Docente
```graphql
mutation loginDocente {
  loginDocenteInvestigador(input: {
    email: "lucia@uca.edu.py",
    password: "1234"
  }) {
    token
    usuario {
      idUsuario
      email
      rolPrincipal
    }
  }
}
```

### Inicio de sesión: Investigador
```graphql
mutation loginInvestigador {
  loginDocenteInvestigador(input: {
    email: "diego@uca.edu.py",
    password: "abcd"
  }) {
    token
    usuario {
      idUsuario
      email
      rolPrincipal
    }
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
    input: { toUserId: 13, skill: "GraphQL", message: "Excelente trabajo en F1" }
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

### Consultar Endorsement
```graphql
query endorsements {
  endorsementsReceived(status: PENDING) {
    idEndorsement
    fromUserId
    toUserId
    skill
    message
    status
    createdAt
  }
}
```

### Consultar Endorsement Dados
```graphql
query endorsementsGiven{
  endorsementsGiven {
    idEndorsement
    toUserId
    skill
    message
    status
  }
}
```

### Aceptar/Rechazar Endorsement
```graphql
mutation endorsementDecision {
  decideEndorsement(id: 6, accept: false) {
    idEndorsement
    status
    decidedAt
  }
}
```
