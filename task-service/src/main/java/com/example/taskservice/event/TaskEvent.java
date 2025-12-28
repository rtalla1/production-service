package com.example.taskservice.event;

import java.time.Instant;
import java.util.UUID;

public record TaskEvent(
    UUID eventId,
    String eventType,
    UUID taskId,
    String title,
    Instant timestamp
) {
    public static TaskEvent created(UUID taskId, String title) {
        return new TaskEvent(UUID.randomUUID(), "CREATED", taskId, title, Instant.now());
    }
}