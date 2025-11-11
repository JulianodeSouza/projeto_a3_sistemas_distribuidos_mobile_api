package com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Método de consulta customizado essencial para o Spring Security.
     * Busca um usuário pelo seu e-mail.
     *
     * O Spring gera: "SELECT u FROM User u WHERE u.email = :email"
     *
     * Usamos 'Optional' porque o e-mail pode não ser encontrado,
     * evitando um NullPointerException.
     *
     * @param email O e-mail (username) a ser buscado.
     * @return Um Optional contendo o User (se encontrado) ou vazio.
     */
    Optional<User> findByEmail(String email);
    
}