package br.com.incode.orderservice.listener;

import br.com.incode.orderservice.event.OrderPlacedEvent;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class OrderPlacedEventListener {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private ObservationRegistry observationRegistry;

    @EventListener
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        log.info("Order Placed Event Received, Sending OrderPlacedEvent to notification-topic: {}", event.getOrderNumber());

        try {
            Objects.requireNonNull(Observation.createNotStarted("notification-topic", this.observationRegistry).observeChecked(() -> {
                CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send("notification-topic", new OrderPlacedEvent(event.getOrderNumber()));

                return future.handle((result, throwable) -> CompletableFuture.completedFuture(result));
            })).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error while sending message to kafka", e);
        }
    }
}
