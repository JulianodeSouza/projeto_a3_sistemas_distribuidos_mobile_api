package com.example.projeto_a3_sistemas_distribuidos_mobile_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.User;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository.UserRepository;

import java.util.ArrayList;

/**
 * Esta classe é responsável por carregar os dados de um usuário
 * (pelo e-mail/username) do banco de dados durante o processo de autenticação.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Método principal chamado pelo Spring Security (AuthenticationManager)
     * no momento do login.
     *
     * @param email O "username" (que no nosso caso é o e-mail) vindo da tentativa de login.
     * @return Um objeto UserDetails (interface do Spring)
     * @throws UsernameNotFoundException Se o usuário não for encontrado no banco.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Busca o usuário no banco de dados pelo e-mail
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));
        
        // Cria e retorna o objeto "UserDetails" padrão do Spring
        // O Spring Security usará este objeto para:
        //    - Comparar a 'senha_do_banco' (user.getPass()) com a 'senha_do_login' (usando o PasswordEncoder)
        //    - Armazenar as permissões (roles) do usuário
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPass(),
                new ArrayList<>()
        );
    }
}