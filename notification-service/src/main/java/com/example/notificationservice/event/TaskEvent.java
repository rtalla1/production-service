package com.example.notificationservice.event;

import java.time.Instant;
import java.util.UUID;

public record TaskEvent(
    UUID eventId,
    String eventType,
    UUID taskId,
    String title,
    Instant timestamp
) {}