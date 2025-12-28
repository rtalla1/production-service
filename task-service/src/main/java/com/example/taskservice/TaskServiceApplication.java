package com.example.taskservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Entry point for the Task Service application.
 */
@SpringBootApplication
public class TaskServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceApplication.class);
    
    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        log.info("Task Service started successfully");
    }
}

