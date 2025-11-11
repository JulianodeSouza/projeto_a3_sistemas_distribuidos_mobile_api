package com.example.projeto_a3_sistemas_distribuidos_mobile_api.controller;

import java.util.List;
import java.util.Optional;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.DebtCreateDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.DebtResponseDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.DebtUpdateDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.service.DebtService;

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

@RestController
@RequestMapping("/api/debt")
public class DebtController {

    @Autowired
    private DebtService debtService;

    // --- READ ---
    @GetMapping
    public List<DebtResponseDTO> getAllDebts() {
        return debtService.getDebtsForCurrentUser();
    }

    // --- CREATE ---
    @PostMapping
    public ResponseEntity<DebtResponseDTO> createDebt(@RequestBody DebtCreateDTO debtDTO) {
        DebtResponseDTO newDebt = debtService.createDebt(debtDTO);
        return ResponseEntity.status(201).body(newDebt);
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<DebtResponseDTO> updateDebt(@PathVariable Long id, @RequestBody DebtUpdateDTO debtDTO) {
        Optional<DebtResponseDTO> updatedDebt = debtService.updateDebt(id, debtDTO);

        return updatedDebt.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDebt(@PathVariable Long id) {
        boolean wasDeleted = debtService.deleteDebt(id);
        if (wasDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}