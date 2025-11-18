package com.example.projeto_a3_sistemas_distribuidos_mobile_api.service;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.UserCreateDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.UserResponseDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.UserUpdateDTO;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.model.User;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponseDTO createUser(UserCreateDTO userDTO) {  
        // Valida email
        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado!");
        }
        // Converte o DTO para Entidade
        User newUser = new User();
        newUser.setName(userDTO.name());
        newUser.setEmail(userDTO.email());
        newUser.setPass(passwordEncoder.encode(userDTO.password()));
        // Salva a Entidade
        User savedUser = userRepository.save(newUser);
        // Converte a Entidade salva para DTO e retorna
        return toUserResponseDTO(savedUser);
    }

    public Optional<UserResponseDTO> updateUser(Long id, UserUpdateDTO userDTO) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Atualiza os campos do DTO
            user.setName(userDTO.name());
            user.setEmail(userDTO.email());
            User updatedUser = userRepository.save(user);
            // Converte e retorna o DTO
            return Optional.of(toUserResponseDTO(updatedUser));
        } else {
            return Optional.empty(); // Retorna vazio se não encontrou
        }
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true; // Sucesso
        } else {
            return false; // Não encontrado
        }
    }

    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        return toUserResponseDTO(user);
    }

    // --- (MAPPER) ---

    /**
     * Limpa os dados antes de enviar para o DTO, diminuindo o risco de enviar senhas.
     */
    private UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(
            user.getName(),
            user.getEmail()
        );
    }
}