# Backend Unificado

Readme para el backend que todos van a usar en sus frontend

## Uso para usuarios de windows:
-Instalar Linux

## Uso para usuarios de Linux :
1-Tener instalado el Java 21.0.8

2-Tener instalado y corriendo el PostgreSQL con las tablas creadas con el .txt que mandó Hector en el grupo de Whatsapp de SE

3-Meter crendeciales de la base de datos local en application.yaml

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

7-Ir a http://localhost:8080/graphiql?path=/graphql 
