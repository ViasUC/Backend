# VIASUC - Sistema Completo (Backend + Frontend)
## Módulo Empresas - Grupo 4

**Trabajo Final - Ingeniería de Software**  
**Universidad Católica Nuestra Señora de la Asunción**  
**Autores:** Grupo 4 - Empresas  
**Año:** 2025

---

## MUY IMPORTANTE - LEER PRIMERO

**RAMAS CORRECTAS PARA USAR:**
- **Frontend**: Rama `main` ← Todo funcionando al 100%
- **Backend**: Rama `fix/empresa-usuario-clave-compuesta` ← Tiene TODOS los cambios finales

Si usan la rama main del backend NO LES VA A FUNCIONAR porque no tiene el merge con nuestros últimos cambios. Asegúrense de hacer checkout a las ramas correctas antes de instalar.

---

## ¿Qué es este proyecto?
Este es nuestro trabajo final para la materia de Ingeniería de Software. Básicamente es un portal completo para que las empresas puedan publicar ofertas de trabajo, solicitar convenios con la universidad y buscar candidatos. 

El proyecto tiene dos partes principales:
- **Backend**: Servidor en Spring Boot con GraphQL que maneja toda la lógica y se conecta a PostgreSQL
- **Frontend**: Aplicación web en Angular donde las empresas interactúan con todo el sistema

### Funcionalidades implementadas:
- Sistema de oportunidades laborales (crear, editar, publicar, pausar, cerrar)
- Módulo de convenios (solicitar, aprobar, gestionar)
- Sistema de endorsements (recomendaciones entre usuarios)
- Perfil público de empresa
- Buscador de portafolios
- Sistema de auditoría para rastrear cambios

## Tecnologías Que Usamos

### Backend:
- **Java 21** - Version LTS
- **Spring Boot 3.x** - Framework principal
- **GraphQL** - Para queries y mutations (Netflix DGS)
- **PostgreSQL** - Base de datos (hosteada en Google Cloud)
- **Hibernate/JPA** - ORM
- **Maven** - Gestor de dependencias

### Frontend:
- **Angular 18** - Framework con componentes standalone
- **TypeScript** - JavaScript tipado
- **Apollo Client** - Cliente GraphQL para Angular
- **SCSS** - Estilos
- **Three.js** - Animaciones 3D en login

## Estructura del Proyecto

### Backend (BackEndOriginal/)
```
src/main/java/com/vias/uc/backend/
├── graphql/
│   ├── OportunidadResolver.java         # Mutations/queries de oportunidades
│   ├── ConvenioResolver.java            # Mutations/queries de convenios  
│   ├── EndorsementResolver.java         # Sistema de endorsements
│   └── PostulacionResolver.java         # Manejo de postulaciones
│
├── model/
│   ├── Oportunidad.java                 # Entidad de ofertas laborales
│   ├── Auditoria.java                   # Registro de cambios
│   ├── Empresa.java, Usuario.java, Convenio.java, etc.
│   └── enums/
│       ├── EstadoOportunidad.java       # activo, borrador, pausada, cerrado
│       ├── EstadoConvenio.java          # Pendiente, Aprobado, Activo, Finalizado
│       └── EstadoEndorsement.java       # PENDING, ACCEPTED, REJECTED
│
├── repository/                          # Queries JPA
├── service/                             # Lógica de negocios
└── controller/                          # Endpoints REST (legacy)

src/main/resources/
├── application.yml                      # Configuración
└── graphql/schema.graphqls              # Schema GraphQL completo
```

### Frontend (ViasucFrontEnd/)
```
src/app/
├── core/                                # Servicios y modelos compartidos
│   ├── models/                          # Interfaces TypeScript
│   ├── services/                        # Servicios globales (auth, convenios, etc)
│   ├── guards/                          # Protección de rutas
│   └── graphql/                         # Queries y mutations GraphQL
│
├── features/                            # Módulos por funcionalidad
│   ├── auth/                            # Login y registro
│   ├── empleador/                       # Dashboard de empresa
│   │   ├── buscar-portafolios/         # Buscar candidatos
│   │   ├── convenios/                  # Gestión de convenios
│   │   ├── empresa-endorsements/       # Sistema endorsements
│   │   └── postulaciones-empresa/      # Ver postulaciones recibidas
│   └── oportunidades/                   # Módulo ofertas laborales
│
└── shared/                              # Componentes reutilizables
```

## Requisitos Previos
### Para el Backend:
- **Java JDK 21**
- **Maven** (incluido como `./mvnw`)
- **PostgreSQL** (usamos una en Google Cloud)
- **Git**

### Para el Frontend:
- **Node.js** (versión 18 o superior)
- **npm** (viene con Node.js)
- **Git**

## Instrucciones de Instalación y Ejecución

### IMPORTANTE: Script Automático Disponible
El repositorio incluye un script bash llamado `restart-services-Original.sh` que levanta automáticamente backend y frontend. **Este archivo estará disponible en un archivo ZIP en el aula virtual junto con este README.**

**ANTES DE EMPEZAR:** Asegúrense de estar en las ramas correctas:
- Frontend: `main`
- Backend: `fix/empresa-usuario-clave-compuesta`

### Opción 1: Usar el Script Automático (Recomendado)
```bash
# Dar permisos de ejecución
chmod +x restart-services-Original.sh

# Ejecutar
./restart-services-Original.sh
```

Este script:
1. Detiene servicios si están corriendo
2. Levanta el backend en puerto 8080
3. Levanta el frontend en puerto 4200
4. Guarda logs en `/logs/`

### Opción 2: Instalación Manual
#### BACKEND
**1. Clonar el repositorio**
```bash
# Clonar
git clone https://github.com/ViasUC/Empresas.git
cd Empresas

# Backend - cambiar a rama correcta
cd BackEndOriginal
git checkout fix/empresa-usuario-clave-compuesta
git pull origin fix/empresa-usuario-clave-compuesta
```

**2. Configurar Base de Datos**
El archivo `src/main/resources/application.yml` ya tiene la configuración de nuestra BD en Google Cloud. La base de datos ya tiene todas las tablas creadas y con datos de prueba.

**3. Compilar y ejecutar**
```bash
# Compilar
./mvnw clean compile

# Ejecutar
./mvnw spring-boot:run
```

El servidor quedará en: `http://localhost:8080`
**4. Verificar**
Ir a: `http://localhost:8080/graphiql?path=/graphql`
Deberías ver el GraphQL Playground.

#### FRONTEND
**1. Verificar rama correcta**
```bash
cd ../ViasucFrontEnd
git checkout main
git pull origin main
```

**2. Instalar dependencias**
```bash
npm install
```
**3. Ejecutar**
```bash
npm start
```
El frontend quedará en: `http://localhost:4200`



## Configuración del Backend (application.yml)
```yaml
server:
  port: 8080
spring:
  datasource:
    url: jdbc:postgresql://34.95.213.224:5432/postgres
    username: postgres
    password: [ver en el código fuente]
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  
  web:
    cors:
      allowed-origins: "http://localhost:4200"
      allowed-methods: GET, POST, PUT, DELETE, OPTIONS
      allowed-headers: "*"

dgs:
  graphql:
    path: /graphql
    graphiql:
      enabled: true
```

## Problemas Comunes y Soluciones

### "Port 8080 already in use"

```bash
# Linux/Mac
kill -9 $(lsof -ti:8080)

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### "Could not create connection to database"
Verificar que PostgreSQL esté corriendo o que la BD en la nube (34.95.213.224:5432) sea accesible.

### Error al compilar
```bash
./mvnw clean install -DskipTests
```

### CORS bloqueando peticiones
Verificar que la configuración CORS esté habilitada en `application.yml` y que el backend esté corriendo.

### Frontend no puede conectarse
1. Verificar backend en `http://localhost:8080`
2. Revisar configuración CORS
3. Ver errores en DevTools del navegador (F12)

## Arquitectura del Sistema
```
┌─────────────────────────────────────┐
│    FRONTEND (Angular 18)            │
│    Puerto: 4200                     │
└───────────┬─────────────────────────┘
            │ HTTP/GraphQL
┌───────────▼─────────────────────────┐
│    BACKEND (Spring Boot 3.x)        │
│    Puerto: 8080                     │
└───────────┬─────────────────────────┘
            │ JDBC
┌───────────▼─────────────────────────┐
│    PostgreSQL                       │
│    Host: 34.95.213.224:5432         │
└─────────────────────────────────────┘
```

## Decisiones Técnicas
**¿Por qué GraphQL?** Permite obtener datos relacionados en una sola query, reduciendo múltiples requests REST.
**¿Por qué Angular 18 standalone?** Componentes standalone más modernos, código más limpio, sin necesidad de NgModules.
**¿Por qué PostgreSQL?** Soporte nativo de enums y tipos personalizados que usamos en el proyecto.
**¿Por qué Maven?** Más verboso pero más fácil de entender, configuración clara.

## Archivos Importantes del Proyecto
**Backend:**
- `OportunidadResolver.java` - Lógica de oportunidades con GraphQL
- `ConvenioResolver.java` - Lógica de convenios
- `EndorsementResolver.java` - Sistema de recomendaciones
- `schema.graphqls` - Schema GraphQL completo
- `application.yml` - Configuración

**Frontend:**
- `src/app/features/empleador/` - Dashboard y funcionalidades
- `src/app/features/oportunidades/` - Gestión de ofertas
- `src/app/core/services/` - Servicios GraphQL

## Resumen Rápido para Instalar (TL;DR)

```bash
# 1. Clonar
git clone https://github.com/ViasUC/Empresas.git
cd Empresas

# 2. Backend (IMPORTANTE: usar rama correcta)
cd BackEndOriginal
git checkout fix/empresa-usuario-clave-compuesta
./mvnw spring-boot:run

# 3. Frontend (en otra terminal)
cd ../ViasucFrontEnd
git checkout main
npm install
npm start

# 4. Abrir http://localhost:4200
```

**Ramas correctas:**
- Frontend: `main`
- Backend: `fix/empresa-usuario-clave-compuesta`

---

## Nota para los Correctores

El script `restart-services-Original.sh` estará disponible en el classrom dentro de un archivo ZIP junto con este README para facilitar la instalación y ejecución del proyecto.

**Grupo 4 - Empresas**  
Trabajo Final - Ingeniería de Software 2025