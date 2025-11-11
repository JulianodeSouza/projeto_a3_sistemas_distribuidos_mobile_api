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

/**
 * Serviço que contém a lógica de negócio para simulação de empréstimos.
 * É chamado pelo LoanSimulationController.
 */
@Service
public class LoanSimulationService {

    @Autowired
    private FinancialInstitutionRepository financialInstitutionRepository;

    /**
     * Executa a simulação de empréstimo comparando as taxas de todas
     * as instituições financeiras cadastradas.
     *
     * @param requestedValue O valor solicitado pelo usuário (ex: 1000.00)
     * @param installments O número de parcelas (ex: 12)
     * @return Uma lista de DTOs (SimulationResultDTO), ordenada do
     * empréstimo mais barato (menor custo total) para o mais caro.
     */
    public List<SimulationResultDTO> simulateLoan(BigDecimal requestedValue, Integer installments) {
        
        // BUSCAR DADOS
        // Busca todas as instituições do banco
        List<FinancialInstitution> institutions = financialInstitutionRepository.findAll();
        List<SimulationResultDTO> results = new ArrayList<>();

        // PROCESSAR DADOS (Loop de Cálculo)
        // Itera sobre cada instituição para calcular o resultado
        for (FinancialInstitution institution : institutions) {
            
            // Converte a taxa de % (ex: 5.0) para decimal (ex: 0.05)
            BigDecimal monthlyRate = institution.getAverageInterestRate().divide(new BigDecimal(100), MathContext.DECIMAL32);

            BigDecimal installmentValue;
            
            // PROTEÇÃO: Verifica se a taxa de juros é zero
            // Se for > 0, usa a fórmula da Tabela Price.
            // Se for == 0, evita divisão por zero e apenas divide o valor.
            if (monthlyRate.compareTo(BigDecimal.ZERO) > 0) {
                // Cálculo da Tabela Price (PMT = PV * i / (1 - (1+i)^-n))
                
                // (1 + i)
                BigDecimal onePlusRate = BigDecimal.ONE.add(monthlyRate);
                // (1 - (1+i)^-n)
                BigDecimal denominator = BigDecimal.ONE.subtract(onePlusRate.pow(-installments, MathContext.DECIMAL32));
                // (PV * i)
                BigDecimal numerator = requestedValue.multiply(monthlyRate);
                
                // PMT = numerator / denominator
                installmentValue = numerator.divide(denominator, RoundingMode.HALF_UP);
            } else {
                // Cálculo simples para juros zero
                installmentValue = requestedValue.divide(new BigDecimal(installments), RoundingMode.HALF_UP);
            }

            // Custo Total = Valor da Parcela * Nro de Parcelas
            BigDecimal totalAmount = installmentValue.multiply(new BigDecimal(installments));

            // Custo Efetivo Total (CET) % = ((TotalPago / ValorInicial) - 1) * 100
            BigDecimal cetPercentage = totalAmount
                    .divide(requestedValue, MathContext.DECIMAL32)
                    .subtract(BigDecimal.ONE)
                    .multiply(new BigDecimal(100))
                    .setScale(2, RoundingMode.HALF_UP); // Arredonda para 2 casas

            // MONTAR RESPOSTA (DTO)
            // Adiciona o DTO com os resultados calculados e formatados
            results.add(new SimulationResultDTO(
                    institution.getName(),
                    institution.getAverageInterestRate(),
                    installmentValue.setScale(2, RoundingMode.HALF_UP),
                    totalAmount.setScale(2, RoundingMode.HALF_UP),
                    cetPercentage
            ));
        }

        // ORDENAR E RETORNAR
        // Ordena a lista de resultados pelo Custo Total (totalAmount),
        // do menor para o maior (mais barato primeiro).
        results.sort((r1, r2) -> r1.totalAmount().compareTo(r2.totalAmount()));

        return results;
    }
}