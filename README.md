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

## F1: Manejo de postulantes
**Transiciones válidas para update endpoints:**
- PENDIENTE	→ ACEPTADA  : Cuando el postulante es seleccionado.
- PENDIENTE	→ RECHAZADA : Cuando no cumple con los requisitos.
- PENDIENTE	→ CANCELADA : Cuando el postulante retira su solicitud o se cierra el proceso.
- ACEPTADA	→ CANCELADA : Si por alguna razón se revoca la aceptación.
- RECHAZADA	→ CANCELADA : Si el registro se anula o la postulación se borra administrativamente.
- CANCELADA	-	No puede cambiar más.
