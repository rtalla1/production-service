# Task Service

A Spring Boot REST API with PostgreSQL, designed for production.

## Highlights

- Layered architecture (Controller → Service → Repository)
- Database migrations with Flyway
- Health probes for container orchestration
- Global exception handling with consistent error responses
- Integration tests against real PostgreSQL
- CI pipeline with GitHub Actions

## Stack

Java 17 · Spring Boot 3.2 · PostgreSQL 14 · Flyway · Docker · GitHub Actions

## Run

```bash
docker-compose up --build -d
curl http://localhost:8080/actuator/health
```

## Test

```bash
docker-compose up -d postgres
./mvnw test
```

## API

| Method | Endpoint          | Description    |
| ------ | ----------------- | -------------- |
| POST   | `/api/tasks`      | Create task    |
| GET    | `/api/tasks/{id}` | Get task by ID |
| GET    | `/api/tasks`      | List all tasks |

Health: `/actuator/health/readiness` · `/actuator/health/liveness`

## Design Decisions

**Flyway + Hibernate validate** — Flyway owns schema. Hibernate validates on startup. No silent drift.

**Fail-fast DB config** — 5s connection timeout. Don't make users wait 30s for an error.

**Readiness includes DB** — If the database is unreachable, don't accept traffic.

**Global exception handler** — Consistent JSON errors. No stack traces to clients.

## Structure

```
controller/TaskController.java    — REST endpoints, validation
service/TaskService.java          — Business logic, transactions
repository/TaskRepository.java    — Data access (Spring Data JPA)
entity/Task.java                  — JPA entity
exception/GlobalExceptionHandler  — @RestControllerAdvice
```
