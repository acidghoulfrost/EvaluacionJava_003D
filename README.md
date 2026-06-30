# TechStore API

Proyecto de la asignatura Java: Diseno y Construccion de Soluciones Nativas en Nube.

Seccion: 003D

Integrantes:
- Cristobal Asencio
- Milovan Rosales

## Descripcion

TechStore API es una API REST desarrollada con Java y Spring Boot.

El proyecto permite iniciar sesion con JWT y administrar productos mediante un CRUD.

En esta evaluacion se agrega despliegue en AWS Academy, uso de Docker, base de datos PostgreSQL, cola SQS, funcion Lambda y GitHub Actions.

## Tecnologias usadas

- Java 17
- Spring Boot
- Maven
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Docker
- Docker Compose
- AWS Academy
- SQS
- Lambda
- ECS Fargate
- API Gateway
- GitHub Actions

## Funcionalidades

- Login con JWT.
- Listar productos.
- Crear productos.
- Modificar productos.
- Eliminar productos de forma logica.
- Enviar auditorias a SQS.
- Procesar auditorias con Lambda.

## Endpoints

Login:

```http
POST /auth/login
```

Productos:

```http
GET /api/productos
POST /api/productos
PUT /api/productos/{id}
DELETE /api/productos/{id}
```

Los endpoints de productos necesitan token:

```http
Authorization: Bearer <TOKEN>
```

## Usuario de prueba

```json
{
  "username": "admin@techstore.cl",
  "password": "Admin1234"
}
```

## Producto

Un producto tiene:

- id
- nombre
- descripcion
- precio
- stock
- categoria
- activo

El DELETE no borra el producto de la base de datos. Solo cambia `activo` a `false`.

## Auditoria

Cuando se crea, modifica o elimina un producto, la API envia un mensaje a SQS.

Nombre de la cola:

```text
techstore-audit-queue
```

La Lambda se encarga de leer esos mensajes y mostrarlos en CloudWatch Logs.

Ejemplo de mensaje:

```json
{
  "accion": "CREAR",
  "productoId": 1,
  "nombre": "Teclado Mecanico",
  "usuario": "admin@techstore.cl",
  "fecha": "2026-06-29T20:30:00Z"
}
```

## Variables de entorno

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
AWS_REGION
SQS_QUEUE_URL
```

No se deben subir claves ni credenciales reales al repositorio.

## Ejecucion local

Compilar y probar:

```powershell
.\mvnw.cmd clean test
```

Ejecutar con Docker:

```powershell
docker compose up --build
```

URL local:

```text
http://localhost:8080
```

## AWS Academy

Para la evaluacion se usa AWS Academy Learner Lab.

El rol usado es:

```text
LabRole
```

No se crean roles IAM personalizados.

La arquitectura esperada usa API Gateway, Load Balancer, ECS Fargate, PostgreSQL, SQS, Lambda y CloudWatch.
