package com.example.projeto_a3_sistemas_distribuidos_mobile_api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.SimulationResultDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.FinancialInstitution;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository.FinancialInstitutionRepository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanSimulationService {

    @Autowired
    private FinancialInstitutionRepository financialInstitutionRepository;

    public List<SimulationResultDTO> simulateLoan(BigDecimal requestedValue, Integer installments) {
        List<FinancialInstitution> institutions = financialInstitutionRepository.findAll();
        List<SimulationResultDTO> results = new ArrayList<>();

        for (FinancialInstitution institution : institutions) {
            // Taxa de juros mensal (ex: 1.99 virá do banco como 1.99, precisamos dividir por 100)
            BigDecimal monthlyRate = institution.getAverageInterestRate().divide(new BigDecimal(100), MathContext.DECIMAL32);

            // Cálculo da Parcela (Fórmula Price simplificada): PMT = PV * i / (1 - (1+i)^-n)
            // Se a taxa for 0, a divisão por zero quebraria, então protegemos:
            BigDecimal installmentValue;
            if (monthlyRate.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
                BigDecimal denominator = BigDecimal.ONE.subtract(onePlusRate.pow(-installments, MathContext.DECIMAL32));
                installmentValue = requestedValue.multiply(monthlyRate).divide(denominator, RoundingMode.HALF_UP);
            } else {
                installmentValue = requestedValue.divide(new BigDecimal(installments), RoundingMode.HALF_UP);
            }

            // Total a pagar = valor da parcela * número de parcelas
            BigDecimal totalAmount = installmentValue.multiply(new BigDecimal(installments));

            // CET (simplificado como a % total de juros sobre o valor original)
            // CET% = ((Total a Pagar / Valor Solicitado) - 1) * 100
            BigDecimal cetPercentage = totalAmount.divide(requestedValue, MathContext.DECIMAL32)
                    .subtract(BigDecimal.ONE)
                    .multiply(new BigDecimal(100))
                    .setScale(2, RoundingMode.HALF_UP);

            results.add(new SimulationResultDTO(
                    institution.getName(),
                    institution.getAverageInterestRate(),
                    installmentValue.setScale(2, RoundingMode.HALF_UP),
                    totalAmount.setScale(2, RoundingMode.HALF_UP),
                    cetPercentage
            ));
        }

        // Ordena pelo menor CET (melhor opção primeiro)
        results.sort((r1, r2) -> r1.totalAmount().compareTo(r2.totalAmount()));

        return results;
    }
}