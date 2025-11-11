package com.example.projeto_a3_sistemas_distribuidos_mobile_api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Serviço responsável por todas as operações relacionadas ao JSON Web Token (JWT).
 * (Gerar, validar, extrair informações).
 */
@Service
public class JwtService {

    // Injeta a chave secreta definida no 'application.properties'
    @Value("${api.security.token.secret}")
    private String secretKey;

    /**
     * Extrai o "username" (que no nosso caso é o e-mail) de um token JWT.
     * O "username" é armazenado no campo 'Subject' (sub) do token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Função genérica para extrair qualquer "claim" (informação) de dentro do token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Gera um token JWT para um usuário (sem claims extras).
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Gera um token JWT com "claims" extras (informações adicionais)
     * para um usuário específico.
     *
     * @param extraClaims Mapa de claims extras a serem adicionados ao token.
     * @param userDetails O objeto UserDetails do Spring (contém o e-mail).
     * @return Uma string de token JWT.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims) // Adiciona os claims extras
                .setSubject(userDetails.getUsername()) // Define o "Subject" (sub) como o e-mail
                .setIssuedAt(new Date(System.currentTimeMillis())) // Data de criação
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Validade: 24 horas
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Assina com a chave
                .compact(); // Constrói o token
    }

    /**
     * Valida um token.
     * Verifica se o "username" do token é o mesmo do UserDetails
     * E se o token não está expirado.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /** Verifica se a data de expiração do token é anterior à data/hora atual. */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /** Extrai a data de expiração do token. */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Faz o "parse" do token usando a chave secreta para extrair
     * todos os "claims" (informações) de dentro dele.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token) // Valida a assinatura
                .getBody();
    }

    /**
     * Converte a 'secretKey' (string) em um objeto 'Key' criptográfico
     * para ser usado na assinatura e validação do token.
     */
    private Key getSignInKey() {
        // A lógica original é um pouco redundante (encode/decode).
        // A forma mais direta, assumindo que a 'secretKey' é um texto simples, seria:
        // return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        
        // Mantendo a lógica original para consistência:
        byte[] keyBytes = Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(secretKey.getBytes()));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}