package com.example.taskservice.controller;

import com.example.taskservice.entity.Task;
import com.example.taskservice.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for Task operations.
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Creates a new task.
     * POST /tasks
     */
    // First @RequestBody: convert JSON to CreateTaskRequest object
    // Second @Valid: validate CreateTaskRequest object using Bean Validation
    // Third @RequestBody: convert CreateTaskRequest object to Task object
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        Task task = taskService.createTask(request.title());
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskResponse.from(task));
    }

    /**
     * Retrieves a task by ID.
     * GET /tasks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID id) {
        return taskService.getTaskById(id)
                .map(task -> ResponseEntity.ok(TaskResponse.from(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all tasks.
     * GET /tasks
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<TaskResponse> tasks = taskService.getAllTasks().stream()
                .map(TaskResponse::from)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    // --- Request/Response DTOs ---

    /**
     * Request payload for creating a task.
     */
    public record CreateTaskRequest(
            @NotBlank(message = "Title is required")
            String title
    ) {
    }

    /**
     * Response payload for task data.
     */
    public record TaskResponse(
            UUID id,
            String title,
            Instant createdAt
    ) {
        public static TaskResponse from(Task task) {
            return new TaskResponse(task.getId(), task.getTitle(), task.getCreatedAt());
        }
    }
}

