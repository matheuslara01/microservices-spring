package br.com.incode.orderservice.dto;

public record InventoryResponse(String skuCode, boolean isInStock) {
}
