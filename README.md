# Evaluacion 2
Asignatura: JAVA: DISENO Y CONSTRUCCION DE SOLUCIONES NATIVAS EN NUBE

Seccion: 003D

Integrantes: Milovan Rosales, Cristobal Asencio


** INDICACIONES GENERALES **

# TechStore API

Microservicio RESTful desarrollado con Java y Spring Boot para administrar productos de TechStore Chile.

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Maven
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Docker
- Docker Compose
- Git/GitHub

## Requisitos

- Java JDK 17 o superior
- Docker Desktop
- Git
- Postman

## Clonar repositorio

git clone https://github.com/acidghoulfrost/Evaluacion2_003D.git

## Ejecutar con Docker Compose (PowerShell)

docker compose up --build

## Generar JAR con Maven (PowerShell)

./mvnw.cmd clean package -DskipTests

## Ejecutar JAR

java -jar target/techstore-api-0.0.1-SNAPSHOT.jar

## Login JWT

POST http://localhost:8080/auth/login

Body:

{
  "username": "admin@techstore.cl",
  "password": "Admin1234"
}

## Endpoints de productos

GET    /api/productos
POST   /api/productos
PUT    /api/productos/{id}
DELETE /api/productos/{id}

Authorization: Bearer <token>
