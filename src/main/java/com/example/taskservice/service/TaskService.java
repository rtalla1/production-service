package com.example.taskservice.service;

import com.example.taskservice.entity.Task;
import com.example.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service layer for Task business logic.
 */
@Service
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Creates a new task with the given title.
     */
    @Transactional
    public Task createTask(String title) {
        Task task = new Task(title);
        return taskRepository.save(task);
    }

    /**
     * Retrieves a task by its ID.
     */
    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }

    /**
     * Retrieves all tasks.
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}

