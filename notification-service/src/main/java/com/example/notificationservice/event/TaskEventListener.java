package com.example.notificationservice.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TaskEventListener {

    private static final Logger log = LoggerFactory.getLogger(TaskEventListener.class);

    // @KafkaListener: when message arrives on "task-events" topic, this method is called
    @KafkaListener(topics = "task-events")
    public void handleTaskEvent(TaskEvent event) {
        log.info("Received event: {} for task {} - '{}'", 
            event.eventType(), event.taskId(), event.title());
        
        // in a real app, we'd send email, push notification, etc.
    }
}