package br.com.incode.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
public record ProductResponse (String id, String name, String description, BigDecimal price) {}

