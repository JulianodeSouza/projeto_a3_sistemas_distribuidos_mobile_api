package com.example.projeto_a3_sistemas_distribuidos_mobile_api.model;

import java.math.BigDecimal;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "debt")
public class Debt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String debtName;

    @Column(nullable = false)
    private BigDecimal totalDebt;

    @Column(nullable = false)
    private BigDecimal monthlyInterestRate;

    @Column(nullable = false)
    private Integer totalInstallments;

    @Column(nullable = false)
    private Integer installmentsPaid;

    @Column(nullable = false)
    private Date dueDate;

    @ManyToOne
    @JoinColumn(name = "financial_institution_id", nullable = false)
    private FinancialInstitution financialInstitution; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Cria a coluna user_id no banco
    @JsonIgnore // Evita que o usuário completo venha junto com cada dívida no JSON
    private User user;
}
