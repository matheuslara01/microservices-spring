package br.com.incode.orderservice.service;

import br.com.incode.orderservice.dto.InventoryResponse;
import br.com.incode.orderservice.dto.OrderLineItemsDto;
import br.com.incode.orderservice.dto.OrderRequest;
import br.com.incode.orderservice.event.OrderPlacedEvent;
import br.com.incode.orderservice.model.Order;
import br.com.incode.orderservice.model.OrderLineItems;
import br.com.incode.orderservice.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;
    @Autowired
    private ObservationRegistry observationRegistry;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public String placeOrder(OrderRequest orderRequest) throws IllegalAccessException {
        var order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.orderLineItemsDtoList().stream().map(this::mapToDto).toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();

        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup",
                this.observationRegistry);
        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");

        return inventoryServiceObservation.observe(() -> {
            InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();

            assert inventoryResponses != null;
            boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                applicationEventPublisher.publishEvent(new OrderPlacedEvent(this, order.getOrderNumber()));
                return "Order Placed";
            } else {
                try {
                    throw new IllegalAccessException("Product is not in stock, please try again later");
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.price());
        orderLineItems.setQuantity(orderLineItemsDto.quantity());
        orderLineItems.setSkuCode(orderLineItemsDto.skuCode());
        return orderLineItems;
    }
}
