package com.example.projeto_a3_sistemas_distribuidos_mobile_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.User;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository.UserRepository;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o usuário no banco pelo email
        User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));
        // Retorna um objeto UserDetails do Spring com os dados do seu usuário
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPass(),
                new ArrayList<>() // Lista de permissões (vazia por enquanto)
        );
    }
}