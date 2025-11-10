package com.example.projeto_a3_sistemas_distribuidos_mobile_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.Debt;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.User;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository.DebtRepository;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository.UserRepository;

@RestController
@RequestMapping("/api/debt")
public class DebtController {

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @GetMapping
    public List<Debt> getAllDebts() {
        User user = getAuthenticatedUser();
        return debtRepository.findByUser(user);
    }

    @PostMapping
    public ResponseEntity<Debt> createDebt(@RequestBody Debt debt) {
        User user = getAuthenticatedUser();
        debt.setUser(user);

        Debt newDebt = debtRepository.save(debt);
        return ResponseEntity.status(201).body(newDebt);
    }

    // --- UPDATE (SEGURO) ---
    @PutMapping("/{id}")
    public ResponseEntity<Debt> updateDebt(@PathVariable Long id, @RequestBody Debt debtDetails) {
        User user = getAuthenticatedUser();
        Optional<Debt> optionalDebt = debtRepository.findById(id);

        if (optionalDebt.isPresent() && optionalDebt.get().getUser().getId().equals(user.getId())) {
            Debt debt = optionalDebt.get();
            debt.setDebtName(debtDetails.getDebtName());
            debt.setFinancialInstitution(debtDetails.getFinancialInstitution());
            debt.setTotalDebt(debtDetails.getTotalDebt());
            debt.setMonthlyInterestRate(debtDetails.getMonthlyInterestRate());
            debt.setTotalInstallments(debtDetails.getTotalInstallments());
            debt.setInstallmentsPaid(debtDetails.getInstallmentsPaid());
            debt.setDueDate(debtDetails.getDueDate());

            return ResponseEntity.ok(debtRepository.save(debt));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // --- DELETE (SEGURO) ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDebt(@PathVariable Long id) {
        User user = getAuthenticatedUser();
        Optional<Debt> debt = debtRepository.findById(id);

        if (debt.isPresent() && debt.get().getUser().getId().equals(user.getId())) {
            debtRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
