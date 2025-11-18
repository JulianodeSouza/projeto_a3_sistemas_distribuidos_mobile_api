package com.example.projeto_a3_sistemas_distribuidos_mobile_api.service;

import org.springframework.stereotype.Component; // <--- IMPORTANTE
import java.math.BigDecimal;
import java.math.RoundingMode;

@Component // <--- TRANSFORMAMOS EM COMPONENTE
public class FinancialMathUtil {
    private static final int SCALE = 6;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    // Removemos 'static' dos métodos
    public BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal monthlyRate, int months) {
        if (monthlyRate.compareTo(BigDecimal.ZERO) <= 0 || months <= 0) {
            if (months > 0) {
                return principal.divide(BigDecimal.valueOf(months), 2, ROUNDING_MODE);
            }
            return BigDecimal.ZERO;
        }

        BigDecimal ratePlusOne = monthlyRate.add(BigDecimal.ONE);
        BigDecimal factor = ratePlusOne.pow(-months).setScale(SCALE, ROUNDING_MODE);
        BigDecimal denominator = BigDecimal.ONE.subtract(factor);
        BigDecimal numerator = principal.multiply(monthlyRate);
        
        return numerator.divide(denominator, 2, ROUNDING_MODE);
    }

    // Removemos 'static' dos métodos
    public BigDecimal calculateTotalInterest(BigDecimal principal, BigDecimal monthlyRate, int months) {
        if (months <= 0) return BigDecimal.ZERO;
        BigDecimal monthlyPayment = calculateMonthlyPayment(principal, monthlyRate, months);
        BigDecimal totalPaid = monthlyPayment.multiply(BigDecimal.valueOf(months));
        return totalPaid.subtract(principal).setScale(2, ROUNDING_MODE);
    }
}