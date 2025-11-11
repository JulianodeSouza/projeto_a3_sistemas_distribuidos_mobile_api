package com.example.projeto_a3_sistemas_distribuidos_mobile_api.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita o CSRF (Cross-Site Request Forgery)
            // Essencial para APIs RESTful "stateless" que usam JWT,
            // pois não dependemos de cookies de sessão.
            .csrf(AbstractHttpConfigurer::disable)

            // Configura a autorização das requisições HTTP
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                // Exige autenticação para qualquer outra requisição (o resto da API)
                .anyRequest().authenticated()
            )
            
            // Configura a política de gerenciamento de sessão
            // Define como STATELESS (sem estado), pois usamos JWT.
            // O Spring Security não vai criar ou usar sessões HTTP.
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Adiciona nosso filtro JWT
            // Informa ao Spring para executar o 'jwtAuthFilter' ANTES do filtro padrão de
            // autenticação de usuário/senha. É aqui que o token é validado.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}