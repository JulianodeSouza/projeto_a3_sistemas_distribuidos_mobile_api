package com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto;

import java.math.BigDecimal;

public record SimulationResultDTO(
    String institutionName,
    BigDecimal monthlyInterestRate,
    BigDecimal installmentValue,
    BigDecimal totalAmount,
    BigDecimal cetPercentage // Custo Efetivo Total (quanto % a mais vai pagar)
) {}