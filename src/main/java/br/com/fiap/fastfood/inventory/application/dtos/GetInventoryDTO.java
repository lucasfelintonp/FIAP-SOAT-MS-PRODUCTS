package br.com.fiap.fastfood.inventory.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record GetInventoryDTO(
    UUID id,
    String name,
    GetUnitDTO unit,
    BigDecimal quantity,
    BigDecimal minimum_quantity,
    String notes,
    LocalDateTime created_at,
    LocalDateTime updated_at
) {
}
