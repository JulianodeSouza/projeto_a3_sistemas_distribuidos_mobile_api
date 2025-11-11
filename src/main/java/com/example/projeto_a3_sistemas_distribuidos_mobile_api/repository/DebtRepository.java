package com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.Debt;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebtRepository extends JpaRepository<Debt, Long>{
    
    /**
     * Método de consulta customizado (Query Method).
     * O Spring Data JPA entende o nome do método e gera a consulta:
     * "SELECT d FROM Debt d WHERE d.user = :user"
     *
     * Usado pelo DebtController para listar apenas as dívidas
     * do usuário autenticado.
     *
     * @param user O objeto User para filtrar as dívidas.
     * @return Uma lista de Dívidas pertencentes ao usuário.
     */
    List<Debt> findByUser(User user);
    
}