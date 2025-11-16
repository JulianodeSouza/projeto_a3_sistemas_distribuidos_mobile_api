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
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // Tenta autenticar. Se a senha/email estiverem errados, o erro acontece AQUI.
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );

            // Se passou da linha acima, deu certo! Gera o token.
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (org.springframework.security.core.AuthenticationException e) {
            // Se der erro na autenticação (senha errada ou user não existe),
            // nós capturamos o erro e forçamos a resposta 401 (UNAUTHORIZED).
            return ResponseEntity.status(401).body("E-mail ou senha inválidos");
        }
    }
}