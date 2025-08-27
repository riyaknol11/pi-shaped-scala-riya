package com.demo.ecommerce.consumer;

import com.demo.ecommerce.model.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderConsumer {

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void handleOrderEvent(OrderEvent orderEvent) {
        try {
            log.info("Received order event: {}", orderEvent.getOrderId());
            processOrder(orderEvent);
            updateInventory(orderEvent);
            sendConfirmationEmail(orderEvent);
            log.info("Successfully processed order: {}", orderEvent.getOrderId());
        } catch (Exception e) {
            log.error("Failed to process order event: {}", orderEvent.getOrderId(), e);
            throw new RuntimeException("Order processing failed", e);
        }
    }

    private void processOrder(OrderEvent orderEvent) {
        log.info("Processing order fulfillment for order: {}", orderEvent.getOrderId());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Order Details:");
        log.info("  Order ID: {}", orderEvent.getOrderId());
        log.info("  Customer ID: {}", orderEvent.getCustomerId());
        log.info("  Customer Email: {}", orderEvent.getCustomerEmail());
        log.info("  Total Amount: ${}", orderEvent.getTotalAmount());
        log.info("  Order Time: {}", orderEvent.getOrderTime());
        log.info("  Items:");

        orderEvent.getItems().forEach(item -> {
            log.info("    - {} x {} (${} each)",
                    item.getQuantity(),
                    item.getProductName(),
                    item.getPrice());
        });

        log.info("Order fulfillment completed for order: {}", orderEvent.getOrderId());
    }

    private void updateInventory(OrderEvent orderEvent) {
        log.info("Updating inventory for order: {}", orderEvent.getOrderId());
        orderEvent.getItems().forEach(item -> {
            log.info("Reducing inventory for product {} by {} units",
                    item.getProductName(), item.getQuantity());
        });
        log.info("Inventory updated for order: {}", orderEvent.getOrderId());
    }

    private void sendConfirmationEmail(OrderEvent orderEvent) {
        log.info("Sending confirmation email for order: {} to {}",
                orderEvent.getOrderId(), orderEvent.getCustomerEmail());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("Confirmation email sent for order: {}", orderEvent.getOrderId());
    }
}