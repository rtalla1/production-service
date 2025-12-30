package com.example.taskservice.service;

import com.example.taskservice.entity.Task;
import com.example.taskservice.event.TaskEvent;
import com.example.taskservice.event.TaskEventProducer;
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
    private final TaskEventProducer taskEventProducer;

    public TaskService(TaskRepository taskRepository, TaskEventProducer taskEventProducer) {
        this.taskRepository = taskRepository;
        this.taskEventProducer = taskEventProducer;
    }

    /**
     * Creates a new task with the given title.
     */
    @Transactional
    public Task createTask(String title) {
        Task task = new Task(title);
        Task saved = taskRepository.save(task);
        taskEventProducer.publish(TaskEvent.created(saved.getId(), saved.getTitle()));
        return saved;
    }

    /**
     * Retrieves a task by its ID.
     */
    public Optional<Task> getTaskById(UUID id) {
        return (id == null) ? Optional.empty() : taskRepository.findById(id);
    }

    /**
     * Retrieves all tasks.
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}

