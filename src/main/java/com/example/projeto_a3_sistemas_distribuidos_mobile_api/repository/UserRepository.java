package com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.User;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    
    
}

