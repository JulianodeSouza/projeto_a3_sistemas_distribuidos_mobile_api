package com.example.projeto_a3_sistemas_distribuidos_mobile_api.model;

import java.math.BigDecimal;
import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    //@OneToMany(
    //    mappedBy = "", 
    //    cascade = CascadeType.ALL,
    //    orphanRemoval = true
    //)
    //@Column(nullable = false)
    //private String IdFinancialInstitution;

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
    
}
