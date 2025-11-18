package com.example.projeto_a3_sistemas_distribuidos_mobile_api.controller;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.FinancialInstitutionResponseDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.FinancialInstitution;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository.FinancialInstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/financial-institutions")
public class FinancialInstitutionController {

    @Autowired
    private FinancialInstitutionRepository repository;

    @GetMapping
    public ResponseEntity<List<FinancialInstitutionResponseDTO>> getAll() {
        List<FinancialInstitution> institutions = repository.findAll();

        // Converte as entidades para DTOs
        List<FinancialInstitutionResponseDTO> dtos = institutions.stream()
                .map(inst -> new FinancialInstitutionResponseDTO(inst.getId(), inst.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}