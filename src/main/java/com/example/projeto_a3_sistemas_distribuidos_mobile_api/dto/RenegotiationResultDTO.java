package com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto;

import java.math.BigDecimal;
import java.util.List;

public record RenegotiationResultDTO(
    List<RenegotiationDebtResultDTO> results,
    BigDecimal totalSavings
) {}