# Task Service

Production-ready REST API demonstrating backend engineering practices: fail-fast configuration, health probes for orchestration, schema migrations, and structured error handling.

**Stack:** Spring Boot 3.2 with Java 17, PostgreSQL, Flyway, Docker, GitHub Actions

## Run

```bash
docker-compose up --build -d
curl http://localhost:8080/actuator/health/readiness
```

## API

| Method | Endpoint          | Description    |
| ------ | ----------------- | -------------- |
| POST   | `/api/tasks`      | Create task    |
| GET    | `/api/tasks/{id}` | Get task by ID |
| GET    | `/api/tasks`      | List all tasks |

Health: `GET /actuator/health/readiness` & `GET /actuator/health/liveness`

## Engineering Decisions

**Flyway + Hibernate validate**  
Flyway owns schema evolution. Hibernate validates entity mappings on startup, catching drift immediately instead of failing silently at runtime.

**Readiness probe includes database**  
Orchestrators shouldn't route traffic to an instance that can't reach its datastore. Readiness checks DB connectivity, liveness doesn't. A DB outage triggers failover, not pod restarts.

**Global exception handler**  
All errors return consistent JSON. Stack traces stay in logs, not in API responses. Invalid UUIDs, validation failures, and 404s all follow the same format.

**CI against real PostgreSQL**  
GitHub Actions spins up a Postgres container. Tests run against the same database engine as production.

## Structure

```
controller/   REST endpoints, request validation, response mapping
service/      Business logic, transaction boundaries
repository/   Data access (Spring Data JPA)
entity/       JPA entities
exception/    @RestControllerAdvice, custom exceptions, error DTOs
```
