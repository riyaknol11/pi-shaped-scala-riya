package com.demo.kafka.service;

import com.demo.kafka.model.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "${app.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(@Payload UserEvent event,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                               @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                               @Header(KafkaHeaders.OFFSET) long offset,
                               @Header(KafkaHeaders.RECEIVED_TIMESTAMP) long timestamp) {

        logger.info("Message received from topic: {}", topic);
        logger.info("Partition: {}, Offset: {}", partition, offset);
        logger.info("Timestamp: {} ({})", timestamp, Instant.ofEpochMilli(timestamp));
        logger.info("Event: {}", event);

        try {
            processBusinessLogic(event);
            logger.info("Message processed successfully");
        } catch (Exception e) {
            logger.error("Error processing message: {}", event, e);
        }
    }

    private void processBusinessLogic(UserEvent event) {
        String eventType = event.getEventType().toLowerCase();
        String userId = event.getUserId();

        switch (eventType) {
            case "user_login" -> {
                logger.info("Processing login for user: {}", userId);
                simulateProcessing(50);
            }
            case "user_logout" -> {
                logger.info("Processing logout for user: {}", userId);
                // Simulate logout processing
                simulateProcessing(30);
            }
            case "purchase" -> {
                logger.info("Processing purchase for user: {}", userId);
                // Simulate purchase processing
                simulateProcessing(100);
            }
            case "page_view" -> {
                logger.info("Processing page view for user: {}", userId);
                // Simulate page view processing
                simulateProcessing(10);
            }
            default -> {
                logger.info("Processing generic event '{}' for user: {}", eventType, userId);
                simulateProcessing(25);
            }
        }
    }

    private void simulateProcessing(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Processing interrupted");
        }
    }
}