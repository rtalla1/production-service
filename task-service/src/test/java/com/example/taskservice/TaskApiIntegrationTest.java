package com.example.taskservice;

import com.example.taskservice.controller.TaskController.CreateTaskRequest;
import com.example.taskservice.controller.TaskController.TaskResponse;
import com.example.taskservice.event.TaskEvent;
import com.example.taskservice.event.TaskEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") // use application-test.yml
class TaskApiIntegrationTest {

    // No-op Kafka producer for tests (no Kafka needed)
    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary // Override the real TaskEventProducer
        public TaskEventProducer taskEventProducer() {
            return new TaskEventProducer(null) {
                @Override
                public void publish(TaskEvent event) {
                    // No-op in tests
                }
            };
        }
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateTask() {
        ResponseEntity<TaskResponse> response = restTemplate.postForEntity(
                "/api/tasks",
                new CreateTaskRequest("Test task"),
                TaskResponse.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        TaskResponse body = Objects.requireNonNull(response.getBody());
        assertThat(body.title()).isEqualTo("Test task");
        assertThat(body.id()).isNotNull();
    }

    @Test
    void shouldReturnNotFoundForNonExistentTask() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/tasks/00000000-0000-0000-0000-000000000000",
                String.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnBadRequestForInvalidUuid() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/tasks/not-a-uuid",
                String.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnBadRequestForMissingTitle() {
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/tasks",
                new CreateTaskRequest(""),
                String.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}