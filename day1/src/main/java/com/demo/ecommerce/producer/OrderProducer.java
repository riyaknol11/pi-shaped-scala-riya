package com.demo.ecommerce.producer;
import com.demo.ecommerce.model.OrderEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${app.rabbitmq.routingkey}")
    private String routingKey;

    public void publishOrderEvent(OrderEvent orderEvent) {
        try {
            log.info("Publishing order event: {}", orderEvent.getOrderId());
            rabbitTemplate.convertAndSend(exchangeName, routingKey, orderEvent);
            log.info("Successfully published order event for order: {}", orderEvent.getOrderId());
        } catch (Exception e) {
            log.error("Failed to publish order event for order: {}", orderEvent.getOrderId(), e);
            throw new RuntimeException("Failed to publish order event", e);
        }
    }
}