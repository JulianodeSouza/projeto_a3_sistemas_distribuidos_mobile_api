package com.example.projeto_a3_sistemas_distribuidos_mobile_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjetoA3SistemasDistribuidosMobileApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetoA3SistemasDistribuidosMobileApiApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}