package com.example.taskservice.repository;

import com.example.taskservice.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for Task persistence operations.
 * Spring Data JPA provides standard CRUD implementations automatically.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
}

