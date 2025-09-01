package com.demo.kafka.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    @NotBlank(message = "Event type is required")
    @JsonProperty("event")
    private String eventType;

    @NotBlank(message = "User ID is required")
    @JsonProperty("user_id")
    private String userId;

    @NotNull
    private Long timestamp;

    public UserEvent() {
        this.timestamp = Instant.now().toEpochMilli();
    }

    public UserEvent(String eventType, String userId) {
        this.eventType = eventType;
        this.userId = userId;
        this.timestamp = Instant.now().toEpochMilli();
    }
}
