# Task Service

A minimal, production-style Spring Boot REST API for managing tasks.

## Tech Stack

- Java 17
- Spring Boot 3.2.1
- Spring Web (REST API)
- Spring Data JPA (Persistence)
- H2 Database (In-memory, for development)

## Project Structure

```
src/main/java/com/example/taskservice/
├── TaskServiceApplication.java   # Application entry point
├── controller/
│   └── TaskController.java       # REST endpoints
├── service/
│   └── TaskService.java          # Business logic
├── repository/
│   └── TaskRepository.java       # Data access layer
└── entity/
    └── Task.java                 # JPA entity
```

## API Endpoints

| Method | Endpoint       | Description        |
|--------|----------------|--------------------|
| POST   | `/tasks`       | Create a new task  |
| GET    | `/tasks/{id}`  | Get task by ID     |
| GET    | `/tasks`       | Get all tasks      |

## Running the Application

```bash
# Build
./mvnw clean package

# Run
./mvnw spring-boot:run
```

The service will start on `http://localhost:8080`.

## Example Requests

**Create a task:**
```bash
curl -X POST http://localhost:8080/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "My first task"}'
```

**Get a task by ID:**
```bash
curl http://localhost:8080/tasks/{id}
```

**Get all tasks:**
```bash
curl http://localhost:8080/tasks
```

## Development Tools

- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:taskdb`
  - Username: `sa`
  - Password: (empty)

