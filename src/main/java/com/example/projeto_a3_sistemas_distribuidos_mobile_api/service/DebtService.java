package com.example.projeto_a3_sistemas_distribuidos_mobile_api.service;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.DebtCreateDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.DebtResponseDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.DebtUpdateDTO;

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

    //Busca o usuário autenticado na requisição.
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado."));
    }

    public List<DebtResponseDTO> getDebtsForCurrentUser() {
        User user = getAuthenticatedUser();
        List<Debt> debts = debtRepository.findByUser(user);
        
        // Converte a lista de Entidades para uma lista de DTOs
        return debts.stream()
                .map(this::toDebtResponseDTO) // Usa nosso mapper privado
                .collect(Collectors.toList());
    }

    public DebtResponseDTO createDebt(DebtCreateDTO debtDTO) {
        User user = getAuthenticatedUser();

        // Busca a Entidade 'FinancialInstitution' a partir do ID do DTO
        FinancialInstitution institution = financialInstitutionRepository.findById(debtDTO.financialInstitutionId())
                .orElseThrow(() -> new RuntimeException("Instituição Financeira não encontrada pelo ID: " + debtDTO.financialInstitutionId()));

        // Converte o DTO para a Entidade 'Debt'
        Debt newDebt = new Debt();
        newDebt.setUser(user); // Associa o usuário logado
        newDebt.setFinancialInstitution(institution); // Associa a instituição encontrada
        
        // Mapeia o resto dos campos
        newDebt.setDebtName(debtDTO.debtName());
        newDebt.setTotalDebt(debtDTO.totalDebt());
        newDebt.setMonthlyInterestRate(debtDTO.monthlyInterestRate());
        newDebt.setTotalInstallments(debtDTO.totalInstallments());
        newDebt.setInstallmentsPaid(debtDTO.installmentsPaid());
        newDebt.setDueDate(debtDTO.dueDate());

        // Salva a nova Entidade
        Debt savedDebt = debtRepository.save(newDebt);

        // Converte a Entidade salva para DTO e retorna
        return toDebtResponseDTO(savedDebt);
    }

    public Optional<DebtResponseDTO> updateDebt(Long id, DebtUpdateDTO debtDTO) {
        User user = getAuthenticatedUser();
        Optional<Debt> optionalDebt = debtRepository.findById(id);

        // Regra de segurança: a dívida existe E pertence ao usuário?
        if (optionalDebt.isPresent() && optionalDebt.get().getUser().getId().equals(user.getId())) {
            
            // Busca a (nova) Entidade 'FinancialInstitution'
            FinancialInstitution institution = financialInstitutionRepository.findById(debtDTO.financialInstitutionId())
                    .orElseThrow(() -> new RuntimeException("Instituição Financeira não encontrada pelo ID: " + debtDTO.financialInstitutionId()));

            // Pega a entidade existente e atualiza
            Debt debt = optionalDebt.get();
            debt.setFinancialInstitution(institution); // Associa a (nova) instituição
            
            // Mapeia o resto dos campos
            debt.setDebtName(debtDTO.debtName());
            debt.setTotalDebt(debtDTO.totalDebt());
            debt.setMonthlyInterestRate(debtDTO.monthlyInterestRate());
            debt.setTotalInstallments(debtDTO.totalInstallments());
            debt.setInstallmentsPaid(debtDTO.installmentsPaid());
            debt.setDueDate(debtDTO.dueDate());

            // Salva a entidade atualizada
            Debt updatedDebt = debtRepository.save(debt);

            // Converte para DTO e retorna
            return Optional.of(toDebtResponseDTO(updatedDebt));
        } else {
            return Optional.empty(); // Não encontrado ou não é o dono
        }
    }

    public boolean deleteDebt(Long id) {
        User user = getAuthenticatedUser();
        Optional<Debt> optionalDebt = debtRepository.findById(id);

        if (optionalDebt.isPresent() && optionalDebt.get().getUser().getId().equals(user.getId())) {
            debtRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // --- (MAPPER) ---

    private DebtResponseDTO toDebtResponseDTO(Debt debt) {
        return new DebtResponseDTO(
            debt.getId(),
            debt.getDebtName(),
            debt.getTotalDebt(),
            debt.getMonthlyInterestRate(),
            debt.getTotalInstallments(),
            debt.getInstallmentsPaid(),
            debt.getDueDate(),
            // Mapeamento importante: enviamos o NOME, não o objeto inteiro
            debt.getFinancialInstitution().getName() 
        );
    }
}