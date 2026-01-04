# Production Service

A microservices backend built to demonstrate production engineering practices. Making things work, and making them work _correctly_.

## Tech Stack

- Java 17, Spring Boot 3.2
- PostgreSQL 14, Flyway migrations
- Apache Kafka (KRaft mode, no Zookeeper)
- Docker, Docker Compose
- GitHub Actions CI

## Architecture

```
┌─────────────────┐       ┌──────────────┐       ┌──────────────────────┐
│   Client        │──────▶│ task-service │──────▶│ notification-service │
│   (curl/app)    │ HTTP  │    :8080     │ Kafka │        :8081         │
└─────────────────┘       └──────┬───────┘       └──────────────────────┘
                                 │
                                 ▼
                          ┌────────────┐
                          │ PostgreSQL │
                          │   :5432    │
                          └────────────┘
```

**task-service**: REST API for task management. It persists to PostgreSQL and publishes events to Kafka.

**notification-service**: Consumes task events from Kafka, which would send emails/push notifications in a real production environment.

## Why These Choices

### Kafka between two services?

It's overkill for this scale. A tightly-coupled architecture with a direct HTTP call would work fine.

I think Kafka is justified when teams need to deploy independently, when downstream systems can't keep up with throughput, or when messages need to be replayed. I implemented it to get hands-on with event-driven patterns. I had to debug serialization mismatches across services, Docker networking issues, and consumer group configuration.

### Flyway instead of Hibernate auto-DDL

`ddl-auto=update` is convenient until it drops a column in production. I chose to use Flyway to give schemas version control-like capabilites and use Hibernate to validate that entities match the schema, allowing drift to be caught immediately.

### Separate health and readiness endpoints

- **Liveness** (`/actuator/health/liveness`): Is the process alive?
- **Readiness** (`/actuator/health/readiness`): Can it serve traffic?

A database outage should stop traffic routing, not trigger a restart loop. These endpoints exist for orchestrators to make that distinction.

### Global exception handler

All errors return the same JSON structure and stack traces stay in logs.

## Running It

```bash
# Start everything
docker-compose up --build -d

# Create a task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"Test task"}'

# Watch the notification service receive the event
docker logs -f notification-service | grep Received
```

## API

| Method | Endpoint                     | Description     |
| ------ | ---------------------------- | --------------- |
| POST   | `/api/tasks`                 | Create a task   |
| GET    | `/api/tasks/{id}`            | Get task by ID  |
| GET    | `/api/tasks`                 | List all tasks  |
| GET    | `/actuator/health/readiness` | Readiness check |
| GET    | `/actuator/health/liveness`  | Liveness check  |

## What I'd Change **at Scale**

**Caching**: Redis for frequent reads

**Authentication**: JWT tokens, rate limiting

**Distributed tracing**: Correlation IDs across services (since debugging cross-service issues currently means grep across multiple log streams)

**Schema registry**: For Kafka message versioning (to not require coordinated deployments)

**Kubernetes**: For auto-scaling and rolling deployments

## Project Structure

```
├── task-service/
│   ├── controller/     # REST endpoints, request/response mapping
│   ├── service/        # Business logic, transaction boundaries
│   ├── repository/     # Data access (Spring Data JPA)
│   ├── entity/         # JPA entities
│   ├── event/          # Kafka producer, event DTOs
│   └── exception/      # Global error handling
├── notification-service/
│   └── event/          # Kafka consumer
├── docker-compose.yml  # Local orchestration (Postgres, Kafka, both services)
└── .github/workflows/  # CI: build, test, Docker image builds
```
