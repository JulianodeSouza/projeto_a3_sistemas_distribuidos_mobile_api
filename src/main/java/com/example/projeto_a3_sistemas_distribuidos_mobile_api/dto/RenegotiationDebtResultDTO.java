package com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto;

import java.math.BigDecimal;

public record RenegotiationDebtResultDTO(
    // Identificação
    String debtId,
    String debtName,
    String creditorName,

    // Original (Antes)
    BigDecimal originalRemainingAmount,
    BigDecimal originalMonthlyPayment,
    BigDecimal originalTotalInterest,
    int originalMonthsRemaining,

    // Negociado (Depois)
    BigDecimal negotiatedAmount,
    BigDecimal negotiatedMonthlyPayment,
    BigDecimal negotiatedTotalInterest,
    int negotiatedTotalMonths,
    BigDecimal savings // Economia total
) {}