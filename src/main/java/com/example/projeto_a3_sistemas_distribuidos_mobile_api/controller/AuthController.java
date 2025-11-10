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

    // DTO simples para receber o login (pode ser uma classe separada também)
    public record LoginRequest(String email, String password) {}
    public record LoginResponse(String token) {}

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // 1. Autentica o usuário (verifica email e senha)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // 2. Se chegou aqui, a autenticação deu certo. Pega os detalhes do usuário.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // 3. Gera o token JWT
        String token = jwtService.generateToken(userDetails);

        // 4. Retorna o token
        return ResponseEntity.ok(new LoginResponse(token));
    }
}