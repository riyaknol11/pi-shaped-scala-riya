package com.demo.kafka.controller;

import com.demo.kafka.model.UserEvent;
import com.demo.kafka.service.KafkaProducer;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    private final KafkaProducer producerService;

    public EventController(KafkaProducer producerService) {
        this.producerService = producerService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> publishEvent(@Valid @RequestBody UserEvent event) {
        logger.info("REST API: Publishing event: {}", event);

        try {
            producerService.sendMessage(event);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Event published successfully",
                    "event", event.toString()
            ));
        } catch (Exception e) {
            logger.error("Failed to publish event: {}", event, e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Failed to publish event: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/simple")
    public ResponseEntity<Map<String, String>> publishSimpleEvent(
            @RequestParam String eventType,
            @RequestParam String userId) {

        logger.info("REST API: Publishing simple event - Type: {}, User: {}", eventType, userId);

        try {
            producerService.sendMessage(eventType, userId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Event published successfully",
                    "eventType", eventType,
                    "userId", userId
            ));
        } catch (Exception e) {
            logger.error("Failed to publish simple event - Type: {}, User: {}", eventType, userId, e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Failed to publish event: " + e.getMessage()
            ));
        }
    }
}