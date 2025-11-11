package com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto;

import java.math.BigDecimal;
import java.sql.Date;

public record DebtUpdateDTO(
    String debtName,
    BigDecimal totalDebt,
    BigDecimal monthlyInterestRate,
    Integer totalInstallments,
    Integer installmentsPaid,
    Date dueDate,
    Long financialInstitutionId
) {}