package com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.Debt;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.User;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long>{
    
    List<Debt> findByUser(User user);
}
