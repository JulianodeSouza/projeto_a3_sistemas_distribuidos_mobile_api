package com.example.projeto_a3_sistemas_distribuidos_mobile_api.config.security;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.service.JwtService;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.service.UserDetailsServiceImpl;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Verifica se o cabeçalho existe e se começa com "Bearer "
        // Se não, é uma requisição sem token (ou formato errado),
        // então apenas a repassamos para o próximo filtro sem autenticar.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrai o token JWT (removendo o prefixo "Bearer ")
        jwt = authHeader.substring(7);

        // Extrai o "username" (neste caso, o e-mail) de dentro do token
        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // Se o token estiver expirado, malformado ou inválido, o jwtService
            // lançará uma exceção. Apenas continuamos a cadeia de filtros.
            // O usuário não será autenticado.
            filterChain.doFilter(request, response);
            return;
        }

        // Se temos o e-mail e o usuário *ainda não foi autenticado* nesta requisição
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Carrega os detalhes do usuário (UserDetails) do banco de dados
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Valida o token:
            // Compara se o usuário do token é o mesmo do banco e se o token não expirou.
            if (jwtService.isTokenValid(jwt, userDetails)) {
                
                // Cria o "ticket" de autenticação padrão do Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Define o usuário como "logado" no contexto de segurança do Spring
                // A partir daqui, a requisição é considerada autenticada.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua a cadeia de filtros (passa para o próximo filtro)
        filterChain.doFilter(request, response);
    }
}