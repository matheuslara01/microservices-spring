package br.com.incode.notificationservice.service;

import br.com.incode.notificationservice.dto.OrderPlacedEvent;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class NotificationListener {

    @Autowired
    private ObservationRegistry observationRegistry;

    @Autowired
    private Tracer tracer;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group", containerFactory = "containerFactory")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent) {
        Observation.createNotStarted("on-message", this.observationRegistry).observe(() -> {
            log.info("Got message <{}>", orderPlacedEvent);
            log.info("TraceId- {}, Received Notification for Order - {}", Objects.requireNonNull(tracer.currentSpan()).context().traceId(), orderPlacedEvent.orderNumber());
        });
        log.info("Received Notification for Order -> {}", orderPlacedEvent.orderNumber());
    }
}
