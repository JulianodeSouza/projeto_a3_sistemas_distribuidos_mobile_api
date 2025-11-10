package com.example.projeto_a3_sistemas_distribuidos_mobile_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.Debt;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository.DebtRepository;

@RestController
@RequestMapping("/api/debt")
public class DebtController {

    @Autowired
    private DebtRepository debtRepository;

    // --- READ ---
    @GetMapping
    public List<Debt> getAllDebts() {
        return debtRepository.findAll();
    }

    // --- READ (Buscar por ID) ---
    @GetMapping("/{id}")
    public ResponseEntity<Debt> getUserById(@PathVariable Long id) {
        Optional<Debt> debt = debtRepository.findById(id);
        return debt.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- CREATE ---
    @PostMapping
    public ResponseEntity<Debt> createDebt(@RequestBody Debt debt) {
        return ResponseEntity.status(201).body(debtRepository.save(debt));
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<Debt> updateDebt(@PathVariable Long id, @RequestBody Debt debtDetails) {
        Optional<Debt> optionalDebt = debtRepository.findById(id);

        if (optionalDebt.isPresent()) {
            Debt debt = optionalDebt.get();
            debt.setDebtName(debtDetails.getDebtName());
            debt.setIdFinancialInstitution(debtDetails.getIdFinancialInstitution());
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

    // --- DELETE  ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDebt(@PathVariable Long id) {
        if (debtRepository.existsById(id)) {
            debtRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
