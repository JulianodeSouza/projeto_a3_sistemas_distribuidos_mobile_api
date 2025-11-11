package com.example.projeto_a3_sistemas_distribuidos_mobile_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.SimulationResultDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.service.LoanSimulationService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/simulations")
public class LoanSimulationController {

    @Autowired
    private LoanSimulationService loanSimulationService;

    //Exemplo de chamada: GET /api/simulations?value=1000.00&installments=12
    @GetMapping
    public ResponseEntity<List<SimulationResultDTO>> simulate(
            @RequestParam BigDecimal value,
            @RequestParam Integer installments) {

        List<SimulationResultDTO> results = loanSimulationService.simulateLoan(value, installments);
        
        return ResponseEntity.ok(results);
    }
}