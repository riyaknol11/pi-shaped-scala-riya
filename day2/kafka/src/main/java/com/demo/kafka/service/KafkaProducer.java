package com.demo.kafka.service;

import com.demo.kafka.model.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, UserEvent> kafkaTemplate;
    private final String topicName;

    public KafkaProducer(KafkaTemplate<String, UserEvent> kafkaTemplate,
                                @Value("${app.kafka.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendMessage(UserEvent event) {
        logger.debug("Sending message: {}", event);

        CompletableFuture<SendResult<String, UserEvent>> future =
                kafkaTemplate.send(topicName, event.getUserId(), event);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                logger.error("Failed to send message: {}", event, exception);
            } else {
                var metadata = result.getRecordMetadata();
                logger.info("Message sent successfully - Topic: {}, Partition: {}, Offset: {}, Event: {}",
                        metadata.topic(), metadata.partition(), metadata.offset(), event);
            }
        });
    }
    public void sendMessage(String eventType, String userId) {
        sendMessage(new UserEvent(eventType, userId));
    }
}