package com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.LoanSimulation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoanSimulationRepository extends JpaRepository<LoanSimulation, Long> {
}
