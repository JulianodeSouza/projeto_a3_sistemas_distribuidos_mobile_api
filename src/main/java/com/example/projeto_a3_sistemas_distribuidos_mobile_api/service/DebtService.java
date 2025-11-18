package com.example.projeto_a3_sistemas_distribuidos_mobile_api.service;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.*;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.Debt;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.FinancialInstitution;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.User;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository.DebtRepository;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository.FinancialInstitutionRepository;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DebtService {

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FinancialInstitutionRepository financialInstitutionRepository;
    
    @Autowired
    private FinancialMathUtil financialMathUtil; // <--- INJETAMOS O UTILITÁRIO AQUI

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado."));
    }

    public List<DebtResponseDTO> getDebtsForCurrentUser() {
        User user = getAuthenticatedUser();
        List<Debt> debts = debtRepository.findByUser(user);
        return debts.stream().map(this::toDebtResponseDTO).collect(Collectors.toList());
    }

    public DebtResponseDTO createDebt(DebtCreateDTO debtDTO) {
        User user = getAuthenticatedUser();
        
        Long institutionId = debtDTO.financialInstitutionId();
        if (institutionId == null) throw new RuntimeException("ID da instituição é obrigatório");

        FinancialInstitution institution = financialInstitutionRepository.findById(institutionId)
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada ID: " + institutionId));

        Debt newDebt = new Debt();
        newDebt.setUser(user);
        newDebt.setFinancialInstitution(institution);
        newDebt.setDebtName(debtDTO.debtName());
        newDebt.setTotalDebt(debtDTO.totalDebt());
        newDebt.setMonthlyInterestRate(debtDTO.monthlyInterestRate());
        newDebt.setTotalInstallments(debtDTO.totalInstallments());
        newDebt.setInstallmentsPaid(debtDTO.installmentsPaid());
        newDebt.setDueDate(debtDTO.dueDate());

        return toDebtResponseDTO(debtRepository.save(newDebt));
    }

    public Optional<DebtResponseDTO> updateDebt(Long id, DebtUpdateDTO debtDTO) {
        User user = getAuthenticatedUser();
        Optional<Debt> optionalDebt = debtRepository.findById(id);

        if (optionalDebt.isPresent() && optionalDebt.get().getUser().getId().equals(user.getId())) {
            
            Long institutionId = debtDTO.financialInstitutionId();
            if (institutionId == null) throw new RuntimeException("ID da instituição é obrigatório");

            FinancialInstitution institution = financialInstitutionRepository.findById(institutionId)
                    .orElseThrow(() -> new RuntimeException("Instituição não encontrada."));
            
            Debt debt = optionalDebt.get();
            debt.setFinancialInstitution(institution);
            debt.setDebtName(debtDTO.debtName());
            debt.setTotalDebt(debtDTO.totalDebt());
            debt.setMonthlyInterestRate(debtDTO.monthlyInterestRate());
            debt.setTotalInstallments(debtDTO.totalInstallments());
            debt.setInstallmentsPaid(debtDTO.installmentsPaid());
            debt.setDueDate(debtDTO.dueDate());

            return Optional.of(toDebtResponseDTO(debtRepository.save(debt)));
        }
        return Optional.empty();
    }

    public boolean deleteDebt(Long id) {
        User user = getAuthenticatedUser();
        Optional<Debt> optionalDebt = debtRepository.findById(id);
        if (optionalDebt.isPresent() && optionalDebt.get().getUser().getId().equals(user.getId())) {
            debtRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // --- SIMULAÇÃO CORRIGIDA (Usando Injeção e verificações de nulo) ---
    public RenegotiationResultDTO simulateRenegotiation(List<RenegotiationRequestDTO> requests) {
        List<RenegotiationDebtResultDTO> results = new ArrayList<>();
        BigDecimal totalSavings = BigDecimal.ZERO;

        for (RenegotiationRequestDTO params : requests) {
            // Converte String ID para Long com segurança
            Long debtId = Long.valueOf(params.debtId());
            
            Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Dívida não encontrada."));

            // 1. DADOS ORIGINAIS
            int originalMonthsRemaining = debt.getTotalInstallments() - debt.getInstallmentsPaid();
            BigDecimal originalPrincipal = debt.getTotalDebt(); 
            BigDecimal originalMonthlyRate = debt.getMonthlyInterestRate().divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

            // Usa a instância injetada 'financialMathUtil'
            BigDecimal origMonthlyPayment = financialMathUtil.calculateMonthlyPayment(originalPrincipal, originalMonthlyRate, originalMonthsRemaining);
            BigDecimal origTotalInterest = financialMathUtil.calculateTotalInterest(originalPrincipal, originalMonthlyRate, originalMonthsRemaining);

            // 2. RENEGOCIAÇÃO
            BigDecimal newPrincipal = originalPrincipal;
            BigDecimal newMonthlyRate = originalMonthlyRate;
            int newMonths = originalMonthsRemaining;
            
            if ("discount".equals(params.type()) && params.discountPercent() != null) {
                BigDecimal discountFactor = BigDecimal.ONE.subtract(BigDecimal.valueOf(params.discountPercent()).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP));
                newPrincipal = originalPrincipal.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
            }
            if ("interest".equals(params.type()) && params.newInterestRate() != null) {
                newMonthlyRate = BigDecimal.valueOf(params.newInterestRate()).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
            }
            if ("extension".equals(params.type()) && params.additionalMonths() != null) {
                newMonths += params.additionalMonths();
            }

            // 3. RESULTADOS
            BigDecimal negMonthlyPayment = financialMathUtil.calculateMonthlyPayment(newPrincipal, newMonthlyRate, newMonths);
            BigDecimal negTotalInterest = financialMathUtil.calculateTotalInterest(newPrincipal, newMonthlyRate, newMonths);
            
            BigDecimal originalTotalCost = originalPrincipal.add(origTotalInterest);
            BigDecimal negotiatedTotalCost = newPrincipal.add(negTotalInterest);
            BigDecimal savings = originalTotalCost.subtract(negotiatedTotalCost).setScale(2, RoundingMode.HALF_UP);
            
            totalSavings = totalSavings.add(savings);

            results.add(new RenegotiationDebtResultDTO(
                debt.getId().toString(), 
                debt.getDebtName(), 
                debt.getFinancialInstitution().getName(),
                originalPrincipal.setScale(2, RoundingMode.HALF_UP), 
                origMonthlyPayment.setScale(2, RoundingMode.HALF_UP), 
                origTotalInterest.setScale(2, RoundingMode.HALF_UP), 
                originalMonthsRemaining,
                newPrincipal.setScale(2, RoundingMode.HALF_UP), 
                negMonthlyPayment.setScale(2, RoundingMode.HALF_UP), 
                negTotalInterest.setScale(2, RoundingMode.HALF_UP), 
                newMonths,
                savings
            ));
        }
        return new RenegotiationResultDTO(results, totalSavings.setScale(2, RoundingMode.HALF_UP));
    }

    private DebtResponseDTO toDebtResponseDTO(Debt debt) {
        return new DebtResponseDTO(
            debt.getId(),
            debt.getDebtName(),
            debt.getTotalDebt(),
            debt.getMonthlyInterestRate(),
            debt.getTotalInstallments(),
            debt.getInstallmentsPaid(),
            debt.getDueDate(),
            debt.getFinancialInstitution().getName() 
        );
    }
}