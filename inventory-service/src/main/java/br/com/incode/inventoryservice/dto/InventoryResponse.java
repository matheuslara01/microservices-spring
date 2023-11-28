package br.com.incode.inventoryservice.dto;

public record InventoryResponse(String skuCode, boolean isInStock) {
}
