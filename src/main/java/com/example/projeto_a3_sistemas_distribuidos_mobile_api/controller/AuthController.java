package com.example.projeto_a3_sistemas_distribuidos_mobile_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.service.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    //DTO (Data Transfer Object) para receber os dados de login, 'record' é uma forma moderna do Java para classes de dados imutáveis.
    public record LoginRequest(String email, String password) {}

    public record LoginResponse(String token) {}

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        
        // Tenta autenticar o usuário usando o email e senha fornecidos.
        // O AuthenticationManager vai chamar o nosso UserDetailsServiceImpl.
        // Se o email ou senha estiverem errados, ele lança uma exceção (que será tratada pelo Spring).
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // Se a autenticação foi bem-sucedida, pegamos os detalhes do usuário
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Geramos um token JWT para este usuário
        String token = jwtService.generateToken(userDetails);

        // Retornamos o token em uma resposta 200
        return ResponseEntity.ok(new LoginResponse(token));
    }
}