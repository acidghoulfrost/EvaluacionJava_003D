# TechStore API - Evaluacion 3 JVY0101

Asignatura: Java: Diseno y Construccion de Soluciones Nativas en Nube
Seccion: 003D
Integrantes: Cristobal Asencio y Milovan Rosales

## Descripcion

TechStore API es un microservicio RESTful desarrollado con Java 17, Spring Boot, Maven, Spring Data JPA, Spring Security, JWT, PostgreSQL, Docker y Docker Compose.

La Evaluacion 3 agrega despliegue cloud en AWS Academy, publicacion de eventos de auditoria en Amazon SQS, procesamiento con AWS Lambda y automatizacion con GitHub Actions.

## Arquitectura cloud

Flujo esperado:

Postman -> API Gateway -> Application Load Balancer -> ECS Fargate -> Contenedor Spring Boot -> RDS PostgreSQL -> SQS -> Lambda -> CloudWatch Logs

Servicios usados:

- Amazon ECR: repositorio privado de la imagen Docker.
- Amazon ECS Fargate: ejecucion del contenedor Spring Boot.
- Application Load Balancer: balanceador para el servicio ECS.
- Amazon API Gateway: entrada publica para Postman.
- Amazon RDS PostgreSQL: base de datos cloud.
- Amazon SQS: cola de auditoria asincrona.
- AWS Lambda: funcion consumidora de mensajes SQS.
- CloudWatch Logs: evidencia de ejecucion serverless.
- GitHub Actions: pipeline CI/CD.

En AWS Academy se debe usar el rol preconfigurado `LabRole`. No crear roles IAM personalizados.

## Endpoints

Login:

```http
POST /auth/login
```

Productos protegidos con JWT:

```http
GET /api/productos
POST /api/productos
PUT /api/productos/{id}
DELETE /api/productos/{id}
```

`GET /api/productos` lista solo productos activos.
`DELETE /api/productos/{id}` realiza eliminacion logica dejando `activo=false`.

## Login JWT

Request:

```json
{
  "username": "admin@techstore.cl",
  "password": "Admin1234"
}
```

Luego enviar el token en los endpoints protegidos:

```http
Authorization: Bearer <TOKEN>
```

## Auditoria asincrona con SQS

Cada operacion de escritura envia un mensaje JSON a la cola:

```text
techstore-audit-queue
```

Operaciones auditadas:

- `POST /api/productos`: accion `CREAR`
- `PUT /api/productos/{id}`: accion `MODIFICAR`
- `DELETE /api/productos/{id}`: accion `ELIMINAR`

Formato del mensaje:

```json
{
  "accion": "CREAR",
  "productoId": 1,
  "nombre": "Teclado Mecanico",
  "usuario": "admin@techstore.cl",
  "fecha": "2026-06-29T20:30:00Z"
}
```

Si SQS no esta configurado o falla, el CRUD igual funciona y la aplicacion deja un log del problema.

## Variables de entorno

Locales y AWS:

```properties
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>:5432/techstore
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=admin123
AWS_REGION=us-east-1
SQS_QUEUE_URL=https://sqs.<region>.amazonaws.com/<account-id>/techstore-audit-queue
```

No subir secretos reales al repositorio.

## Ejecucion local con Docker Compose

```powershell
docker compose up --build
```

La API queda disponible en:

```text
http://localhost:8080
```

En local `SQS_QUEUE_URL` queda vacio, por lo que la API funciona sin AWS y solo muestra un warning cuando intente auditar.

## Dockerfile multi-stage

El Dockerfile compila con Maven y luego ejecuta con una imagen JRE liviana:

- Java 17.
- Puerto 8080.
- Usuario no-root.
- Imagen mas liviana y reproducible.

Comando manual:

```powershell
docker build -t techstore-api .
```

## Lambda

La funcion se llama:

```text
techstore-audit-logger
```

Archivo incluido:

```text
notification-function/lambda_function.py
```

Configuracion recomendada:

- Runtime: Python 3.x.
- Execution role: `LabRole`.
- Trigger: SQS `techstore-audit-queue`.

La evidencia se revisa en CloudWatch Logs. Deben verse `accion`, `productoId`, `nombre`, `usuario` y `fecha`.

## AWS Academy - pasos resumidos

1. Iniciar Learner Lab y copiar credenciales temporales.
2. Crear RDS PostgreSQL con base `techstore`.
3. Crear SQS estandar `techstore-audit-queue`.
4. Crear Lambda `techstore-audit-logger` usando `LabRole`.
5. Agregar trigger SQS a Lambda.
6. Crear ECR privado `techstore-api`.
7. Construir y subir imagen Docker a ECR.
8. Crear cluster ECS Fargate.
9. Crear Task Definition con imagen ECR y puerto 8080.
10. Usar `LabRole` como Task Role y Task Execution Role.
11. Crear servicio ECS con Application Load Balancer publico.
12. Crear API Gateway con ruta `ANY /{proxy+}` hacia el ALB.
13. Probar desde Postman usando la URL publica de API Gateway.

## Variables en ECS

En la Task Definition configurar:

```text
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
AWS_REGION
SQS_QUEUE_URL
```

## Seguridad de red

- API Gateway es el punto publico de entrada.
- ALB recibe trafico publico por HTTP.
- ECS Tasks reciben trafico 8080 solo desde el Security Group del ALB.
- RDS PostgreSQL recibe 5432 solo desde el Security Group de ECS.
- No se deben publicar credenciales AWS en codigo ni en README.
- En AWS Academy se debe usar `LabRole`.

## Auto Scaling

Configuracion recomendada para la defensa:

- Minimo: 1 tarea.
- Deseado: 1 tarea.
- Maximo: 2 o 3 tareas.
- Politica por CPU: 60% o 70%.

ECS Fargate reinicia tareas si fallan y permite escalar sin administrar servidores.

## GitHub Actions

Workflow:

```text
.github/workflows/deploy.yml
```

Se ejecuta con push a `main` y realiza:

1. Checkout.
2. Java 17.
3. PostgreSQL de prueba.
4. `mvn clean test`.
5. Login AWS.
6. Build Docker.
7. Push a ECR.
8. Nuevo despliegue ECS.

Secrets requeridos:

```text
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
AWS_SESSION_TOKEN
AWS_REGION
AWS_ACCOUNT_ID
ECS_CLUSTER
ECS_SERVICE
```

En AWS Academy las credenciales son temporales. Si se reinicia el Learner Lab, actualizar los secrets, especialmente `AWS_SESSION_TOKEN`.

## Pruebas Postman

Usar la URL de API Gateway, por ejemplo:

```text
https://<api-id>.execute-api.<region>.amazonaws.com
```

Login:

```http
POST /auth/login
```

Crear producto:

```http
POST /api/productos
Authorization: Bearer <TOKEN>
```

Body:

```json
{
  "nombre": "Teclado Mecanico",
  "descripcion": "Teclado RGB para gaming",
  "precio": 29990,
  "stock": 15,
  "categoria": "Perifericos"
}
```

Modificar:

```http
PUT /api/productos/{id}
Authorization: Bearer <TOKEN>
```

Eliminar logico:

```http
DELETE /api/productos/{id}
Authorization: Bearer <TOKEN>
```

Validar seguridad:

- Sin token, `/api/productos` debe fallar.
- Con token valido, debe funcionar.

## Evidencias para el video

Mostrar en 3 a 8 minutos:

1. Repositorio GitHub con cambios.
2. Dockerfile multi-stage.
3. GitHub Actions exitoso.
4. Imagen `techstore-api:latest` en ECR.
5. ECS con servicio activo y task healthy.
6. ALB creado.
7. API Gateway apuntando al ALB.
8. Postman: login, GET, POST, PUT y DELETE.
9. SQS `techstore-audit-queue`.
10. Lambda `techstore-audit-logger` con trigger SQS.
11. CloudWatch Logs con datos de auditoria.
12. Explicacion de `LabRole`, Security Groups y Auto Scaling.

## Docker Swarm opcional

Si el docente pide evidencia minima de Docker Swarm:

```powershell
docker swarm init
docker stack deploy -c docker-compose.yml techstore
docker service ls
docker service scale techstore_microservicio=3
docker stack rm techstore
docker swarm leave --force
```

La prioridad de esta entrega es AWS Academy con ECS Fargate, SQS, Lambda, API Gateway y GitHub Actions.
