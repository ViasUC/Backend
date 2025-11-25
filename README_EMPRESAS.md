# VIASUC - Backend (Módulo Empresas)

**Trabajo Final - Ingeniería de Software**  
**Universidad Católica Nuestra Señora de la Asunción**  
**Autores:** Grupo 4 - Empresas  
**Año:** 2025

---

## ¿Qué es esto?

Este es el backend del portal VIASUC, específicamente la parte que maneja todo lo relacionado con empresas. Es un servidor REST + GraphQL hecho en Spring Boot que se conecta a PostgreSQL.

Nosotros (Grupo Empresas) trabajamos principalmente en:
- Gestión de Oportunidades Laborales (CRUD completo con estados)
- Sistema de Auditoría para cambios de estado
- Registro y autenticación de empresas
- Integración con el módulo de Convenios

## Tecnologías Usadas

- **Java 21** - Versión LTS más reciente
- **Spring Boot 3.x** - Framework principal
- **GraphQL** - Para las queries y mutations (con DGS de Netflix)
- **PostgreSQL** - Base de datos relacional
- **Hibernate/JPA** - ORM para manejar la BD
- **Maven** - Gestión de dependencias

## Estructura del Proyecto (Lo Importante)

```
src/main/java/com/vias/uc/backend/
├── graphql/
│   └── OportunidadResolver.java         # Mutations y queries de oportunidades
│
├── model/
│   ├── Oportunidad.java                 # Entidad principal
│   ├── Auditoria.java                   # Registro de cambios
│   ├── Empresa.java                     # Datos de empresa
│   ├── Usuario.java                     # Usuarios del sistema
│   └── enums/
│       └── EstadoOportunidad.java       # Enum: activo, borrador, pausada, cerrado
│
├── repository/
│   ├── OportunidadRepository.java       # Queries JPA
│   ├── AuditoriaRepository.java         
│   ├── EmpresaRepository.java
│   └── EmpresaUsuarioRepository.java    # Relación empresa-usuario
│
├── service/
│   ├── ConvenioService.java             # Lógica de convenios
│   └── AuthorizationService.java        # Validaciones de permisos
│
└── controller/
    └── ConvenioController.java          # Endpoints REST de convenios

src/main/resources/
├── application.yml                       # Configuración de BD y server
└── graphql/
    └── schema.graphqls                  # Schema GraphQL (tipos, queries, mutations)
```

## Requisitos Previos

Para levantar el BACKEND necesitas:
- **Java JDK 21** - El proyecto usa Java 21 específicamente
- **Maven** - Para compilar (viene con el proyecto como `./mvnw`)
- **PostgreSQL** - Base de datos corriendo
- **Git** - Para clonar el repo

Nota: No necesitas instalar Spring Boot por separado, Maven lo descarga automáticamente.

## Instalación y Configuración

### 1. Clonar el repositorio
```bash
git clone https://github.com/ViasUC/Backend.git
cd Backend
```

### 2. Configurar Base de Datos

Editar el archivo `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/viasuc_db
    username: TU_USUARIO
    password: TU_PASSWORD
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

**Importante:** La base de datos debe tener las tablas creadas. El script SQL lo pasó Héctor por el grupo.

### 3. Compilar el proyecto

```bash
./mvnw clean compile
```

Si estás en Windows:
```bash
mvnw.cmd clean compile
```

### 4. Levantar el servidor

```bash
./mvnw spring-boot:run
```

El servidor arranca en: `http://localhost:8080`

### 5. Probar GraphQL

Ir a: `http://localhost:8080/graphiql?path=/graphql`

Ahí puedes ejecutar queries y mutations directamente.

## Lo Que Implementamos (Módulo Empresas)

### 1. Gestión de Oportunidades

El módulo principal que desarrollamos. Una oportunidad puede ser una pasantía, trabajo tiempo completo, etc.

#### Estados de una Oportunidad
```
activo    -> Publicada, visible para candidatos
borrador  -> Recién creada, solo visible para la empresa
pausada   -> Temporalmente oculta
cerrado   -> Finalizada, no se puede reabrir
```

Estos valores están en el enum de PostgreSQL:
```sql
CREATE TYPE estado_oportunidad AS ENUM ('activo', 'borrador', 'pausada', 'cerrado');
```

#### Crear una Oportunidad

```graphql
mutation CrearOportunidad {
  crearOportunidadDocente(input: {
    idCreador: "123"
    titulo: "Desarrollador Junior"
    descripcion: "Buscamos desarrollador con conocimientos en Java"
    requisitos: "Java, Spring Boot, SQL"
    ubicacion: "Asunción"
    modalidad: "HIBRIDO"
    tipo: "TIEMPO_COMPLETO"
    fechaCierre: "2025-12-31T23:59:00"
  }) {
    idOportunidad
    titulo
    estado
  }
}
```

**Nota:** El estado por defecto es `borrador`. Luego se puede cambiar con `cambiarEstadoOportunidad`.

#### Editar una Oportunidad

```graphql
mutation EditarOportunidad {
  editarOportunidad(input: {
    idOportunidad: 95
    idEditor: "123"
    titulo: "Desarrollador Junior (ACTUALIZADO)"
    descripcion: "Nueva descripción"
    fechaCierre: "2025-12-31T23:59:00"
  }) {
    idOportunidad
    titulo
    estado
  }
}
```

**Restricción:** Solo se puede editar si está en estado `borrador`. Si está en otro estado, solo puedes cambiar el estado.

#### Cambiar Estado (con Auditoría)

```graphql
mutation CambiarEstado {
  cambiarEstadoOportunidad(
    idOportunidad: 95
    nuevoEstado: activo
    idActor: "123"
  ) {
    idOportunidad
    estado
  }
}
```

Esto automáticamente crea un registro en la tabla `auditoria` con:
- Quién lo cambió (`idActor`)
- De qué estado a qué estado
- Cuándo se hizo el cambio
- Acción: "CAMBIAR_ESTADO_OPORTUNIDAD"

#### Listar Oportunidades de una Empresa

```graphql
query ListarOportunidades {
  oportunidadesPorEmpresa(idEmpresa: 13) {
    idOportunidad
    titulo
    estado
    fechaPublicacion
    creador {
      nombre
      apellido
    }
  }
}
```

### 2. Sistema de Auditoría

Cada vez que se crea o modifica una oportunidad, se crea un registro de auditoría:

```java
@Entity
@Table(name = "auditoria")
public class Auditoria {
    private Integer idAuditoria;
    private Integer actorId;        // Quién hizo el cambio
    private String accion;          // Ej: "CREAR_OPORTUNIDAD", "CAMBIAR_ESTADO_OPORTUNIDAD"
    private String detalle;         // Descripción del cambio
    private LocalDateTime timestamp; // Cuándo pasó
}
```

Esto nos permite tener un historial completo de todos los cambios.

### 3. Validaciones Implementadas

#### En OportunidadResolver.java:

1. **Validación de Rol:** Solo usuarios con rol `administrador`, `profesor`, `investigador` o `empresa` pueden crear oportunidades.

```java
private void assertRolHabilitado(Usuario usuario) {
    String rol = usuario.getRolPrincipal().name().toLowerCase();
    if (!Set.of("administrador", "profesor", "investigador", "empresa").contains(rol)) {
        throw new AccessDeniedException("No autorizado");
    }
}
```

2. **Validación de Permisos:** Solo el creador o un administrador pueden editar/cambiar estado.

```java
boolean esCreador = actor.getIdUsuario().longValue() == op.getIdCreador().longValue();
boolean esAdmin = "administrador".equals(rolActor);
if (!esCreador && !esAdmin) {
    throw new AccessDeniedException("Solo el creador o admin pueden modificar");
}
```

3. **Validación de Fecha:** La fecha de cierre no puede ser en el pasado.

```java
if (cierre != null && cierre.isBefore(LocalDateTime.now())) {
    throw new IllegalArgumentException("fechaCierre no puede ser en el pasado");
}
```

### 4. Módulo de Convenios (Integrado)

Trabajo del compañero Federico. Incluye:
- `ConvenioController.java` - Endpoints REST
- `ConvenioService.java` - Lógica de negocio
- `Convenio.java` - Modelo de entidad

Endpoints principales:
- `POST /api/v1/convenios/solicitar` - Solicitar convenio
- `GET /api/v1/convenios/mis-solicitudes` - Ver solicitudes
- `GET /api/v1/convenios/vigentes` - Convenios aprobados

## Problemas que Resolvimos

### 1. Enum de Estados en PostgreSQL

**Problema:** PostgreSQL usa un tipo ENUM personalizado que debe coincidir exactamente con el Java enum.

**Solución:** Creamos el enum en Java con los mismos valores:
```java
public enum EstadoOportunidad {
    activo,
    borrador,
    pausada,
    cerrado
}
```

### 2. LazyInitializationException con Empresa

**Problema:** Al intentar acceder a `oportunidad.getEmpresa().getNombreEmpresa()` fuera de la sesión de Hibernate, tiraba error.

**Solución:** Agregamos un try-catch y manejamos el caso cuando empresa es null:
```java
@SchemaMapping(typeName = "Oportunidad", field = "empresa")
public String getEmpresaNombre(Oportunidad oportunidad) {
    try {
        if (oportunidad.getEmpresa() != null) {
            return oportunidad.getEmpresa().getNombreEmpresa();
        }
    } catch (Exception e) {
        return null;
    }
    return null;
}
```

### 3. Comparación de IDs (Long vs Integer)

**Problema:** `usuario.getIdUsuario()` devuelve `Long` pero `oportunidad.getIdCreador()` es `Integer`, entonces `.equals()` fallaba.

**Solución:** Convertimos ambos a long antes de comparar:
```java
boolean esCreador = actor.getIdUsuario().longValue() == op.getIdCreador().longValue();
```

## Estructura de la Base de Datos

### Tabla: oportunidades
```sql
CREATE TABLE oportunidades (
    id_oportunidad SERIAL PRIMARY KEY,
    id_creador INTEGER NOT NULL,
    id_empresa INTEGER,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    requisitos TEXT,
    ubicacion VARCHAR(255),
    modalidad VARCHAR(50),
    tipo VARCHAR(50),
    fecha_publicacion TIMESTAMP,
    fecha_cierre TIMESTAMP,
    estado estado_oportunidad DEFAULT 'borrador',
    id_auditoria INTEGER,
    FOREIGN KEY (id_creador) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_empresa) REFERENCES empresas(id_empresa),
    FOREIGN KEY (id_auditoria) REFERENCES auditoria(id_auditoria)
);
```

### Tabla: auditoria
```sql
CREATE TABLE auditoria (
    id_auditoria SERIAL PRIMARY KEY,
    actor_id INTEGER NOT NULL,
    accion VARCHAR(100) NOT NULL,
    detalle TEXT,
    timestamp TIMESTAMP DEFAULT NOW()
);
```

## Cómo Probar Todo el Flujo

### 1. Crear una oportunidad (queda en BORRADOR)
```graphql
mutation {
  crearOportunidadDocente(input: {
    idCreador: "1052"
    titulo: "Test Oportunidad"
    descripcion: "Descripción de prueba"
    ubicacion: "Asunción"
    modalidad: "PRESENCIAL"
    tipo: "PASANTIA"
  }) {
    idOportunidad
    estado
  }
}
```

### 2. Publicar (cambiar a ACTIVO)
```graphql
mutation {
  cambiarEstadoOportunidad(
    idOportunidad: 1
    nuevoEstado: activo
    idActor: "1052"
  ) {
    estado
  }
}
```

### 3. Verificar que NO se puede editar
```graphql
mutation {
  editarOportunidad(input: {
    idOportunidad: 1
    idEditor: "1052"
    titulo: "Intento editar estando ACTIVO"
  }) {
    titulo
  }
}
```
Esto debería funcionar SOLO para cambios menores. Los cambios grandes requieren volver a BORRADOR.

### 4. Verificar auditoría en la BD
```sql
SELECT * FROM auditoria WHERE actor_id = 1052 ORDER BY timestamp DESC;
```

Deberías ver registros de:
- CREAR_OPORTUNIDAD
- CAMBIAR_ESTADO_OPORTUNIDAD

## Endpoints REST (Convenios)

Además de GraphQL, hay endpoints REST para convenios:

```bash
# Solicitar convenio
POST http://localhost:8080/api/v1/convenios/solicitar
Content-Type: application/json

{
  "idUsuario": 1052,
  "institucion": "Universidad Católica",
  "descripcion": "Convenio para pasantías"
}

# Ver mis solicitudes
GET http://localhost:8080/api/v1/convenios/mis-solicitudes
X-User-Id: 1052

# Ver convenios vigentes
GET http://localhost:8080/api/v1/convenios/vigentes
X-User-Id: 1052
```

## Configuración Importante

### application.yml
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:postgresql://34.95.213.224:5432/postgres
    username: postgres
    password: [REDACTED]
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

dgs:
  graphql:
    path: /graphql
    graphiql:
      enabled: true
```

## Troubleshooting

### Error: "Port 8080 already in use"
Matar el proceso:
```bash
kill -9 $(lsof -ti:8080)
```

### Error: "Could not create connection to database"
Verificar que PostgreSQL esté corriendo y las credenciales sean correctas en `application.yml`.

### Error: "No session" (Hibernate)
Esto pasa cuando intentas acceder a relaciones lazy fuera de una transacción. Solución: usar `@Transactional` o cargar la relación con EAGER.

### Error al compilar GraphQL schema
Borrar la carpeta `target/` y recompilar:
```bash
rm -rf target
./mvnw clean compile
```

## Contacto
Si algo no funciona o tienen dudas para la corrección:
- Email: alfre_costas@hotmail.com
- GitHub Backend: https://github.com/ViasUC/Backend
- Rama que usamos: `fix/empresa-usuario-clave-compuesta`

---

**Nota para los correctores:** 

El código está en la rama `fix/empresa-usuario-clave-compuesta` que tiene integrado:
1. Nuestro módulo de oportunidades con estados y auditoría
2. El módulo de convenios de Federico
3. Todas las correcciones de bugs que encontramos

La parte más importante está en:
- `OportunidadResolver.java` - Toda la lógica de oportunidades
- `schema.graphqls` - Definición de tipos y mutations
- `EstadoOportunidad.java` - El enum que coincide con PostgreSQL

Gracias por revisar nuestro trabajo.
