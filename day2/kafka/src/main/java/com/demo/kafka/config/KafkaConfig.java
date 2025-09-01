package com.demo.kafka.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.topic.name}")
    private String topicName;

    @Value("${app.kafka.topic.partitions}")
    private int partitions;

    @Value("${app.kafka.topic.replication-factor}")
    private int replicationFactor;

    @Bean
    public NewTopic demoTopic() {
        return TopicBuilder.name(topicName)
                .partitions(partitions)
                .replicas(replicationFactor)
                .compact() // Enable log compaction for better performance
                .build();
    }
}