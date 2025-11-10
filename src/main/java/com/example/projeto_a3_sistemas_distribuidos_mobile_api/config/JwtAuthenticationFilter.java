package com.example.projeto_a3_sistemas_distribuidos_mobile_api.config;

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

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.service.JwtService;
import com.example.projeto_a3_sistemas_distribuidos_mobile_api.service.UserDetailsServiceImpl;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Busca o header "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Se não tem header ou não começa com "Bearer ", passa para o próximo filtro (não autentica)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extrai o token (remove o prefixo "Bearer ")
        jwt = authHeader.substring(7);

        // 4. Extrai o email do token
        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (Exception e) {
             // Se o token for inválido ou expirado, jwtService pode lançar exceção.
             // Nesse caso, apenas seguimos sem autenticar.
            filterChain.doFilter(request, response);
            return;
        }

        // 5. Se achou o email e ainda não está autenticado no contexto do Spring
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do usuário do banco
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Valida se o token pertence mesmo a esse usuário e não expirou
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // 7. Cria o objeto de autenticação do Spring
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 8. Salva a autenticação no contexto (usuário está logado!)
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 9. Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}