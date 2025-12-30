package com.example.taskservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

// @Component: Spring makes TaskEventProducer at startup and injects KafkaTemplate from application.yml (auto-configured)
// also makes it available for autowiring in other components like TaskService
@Component
public class TaskEventProducer {

    private static final Logger log = LoggerFactory.getLogger(TaskEventProducer.class);
    private static final String TOPIC = "task-events";

    // Spring's Kafka client, used to send messages to Kafka
    private final KafkaTemplate<String, TaskEvent> kafkaTemplate;

    public TaskEventProducer(KafkaTemplate<String, TaskEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @SuppressWarnings("null") // TOPIC is a constant, taskId is checked by requireNonNull
    public void publish(TaskEvent event) {
        log.info("Publishing event: {} for task {}", event.eventType(), event.taskId());
        // use task id as the key to ensure order of events for that task, even across multiple partitions
        kafkaTemplate.send(TOPIC, Objects.requireNonNull(event.taskId()).toString(), event);
    }
}