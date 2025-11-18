package com.example.projeto_a3_sistemas_distribuidos_mobile_api.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto.ApiErrorDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Erro de Login (Senha/Email errados) -> 401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorDTO> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse("E-mail ou senha inválidos", HttpStatus.UNAUTHORIZED);
    }

    // 2. Erro de Acesso Negado (Sem token ou token inválido) -> 403
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorDTO> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse("Acesso negado. Você não tem permissão ou seu token expirou.", HttpStatus.FORBIDDEN);
    }

    // 3. Erros de Regra de Negócio (ex: "E-mail já cadastrado")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorDTO> handleRuntimeException(RuntimeException ex) {
        // Se for conflito (já existe)
        if (ex.getMessage().toLowerCase().contains("cadastrado") || ex.getMessage().toLowerCase().contains("existe")) {
            return buildResponse(ex.getMessage(), HttpStatus.CONFLICT); // 409
        }
        
        // Se for recurso não encontrado (ex: id não existe)
        if (ex.getMessage().toLowerCase().contains("não encontrado")) {
            return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND); // 404
        }

        // Outros erros de execução
        return buildResponse("Erro de processamento: " + ex.getMessage(), HttpStatus.BAD_REQUEST); // 400
    }

    // 4. O "Pega-Tudo" (Qualquer outro erro que não previmos) -> 500
    // Isso garante que o Front NUNCA receba HTML ou texto puro. Sempre JSON.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGeneralException(Exception ex) {
        // É boa prática logar o erro no console do servidor para você saber o que houve
        ex.printStackTrace(); 
        return buildResponse("Ocorreu um erro interno no servidor. Tente novamente mais tarde.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // --- Método Auxiliar para montar a resposta JSON rapidinho ---
    private ResponseEntity<ApiErrorDTO> buildResponse(String message, HttpStatus status) {
        ApiErrorDTO erro = new ApiErrorDTO(message, status.value());
        return ResponseEntity.status(status).body(erro);
    }
}