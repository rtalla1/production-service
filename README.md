# Task Service

Production-ready REST API demonstrating backend engineering practices: fail-fast configuration, health probes for orchestration, schema migrations, and structured error handling.

**Stack:** Java 17 · Spring Boot 3.2 · PostgreSQL · Flyway · Docker · GitHub Actions

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

Health: `GET /actuator/health/readiness` · `GET /actuator/health/liveness`

## Engineering Decisions

**Flyway + Hibernate validate**  
Flyway owns schema evolution. Hibernate validates entity mappings on startup — catches drift immediately instead of failing silently at runtime.

**Fail-fast database config**  
5-second connection timeout instead of the 30-second default. Users shouldn't wait half a minute to learn the database is down.

**Readiness probe includes database**  
Kubernetes (or any orchestrator) shouldn't route traffic to an instance that can't reach its datastore. Readiness checks DB connectivity, liveness doesn't — so a DB outage triggers failover, not pod restarts.

**Global exception handler**  
All errors return consistent JSON. Stack traces stay in logs, not in API responses. Invalid UUIDs, validation failures, and 404s all follow the same format.

**CI against real PostgreSQL**  
GitHub Actions spins up a Postgres container. Tests run against the same database engine as production — no H2 surprises.

## Structure

```
controller/   REST endpoints, request validation, response mapping
service/      Business logic, transaction boundaries
repository/   Data access (Spring Data JPA)
entity/       JPA entities
exception/    @RestControllerAdvice, custom exceptions, error DTOs
```
