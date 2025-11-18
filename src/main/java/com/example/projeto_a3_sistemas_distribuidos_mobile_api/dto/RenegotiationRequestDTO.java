package com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto;

public record RenegotiationRequestDTO(
    String debtId, // O ID da dívida que o usuário está negociando
    String type, // 'discount', 'interest', 'extension'
    Double discountPercent,
    Double newInterestRate,
    Integer additionalMonths
) {}