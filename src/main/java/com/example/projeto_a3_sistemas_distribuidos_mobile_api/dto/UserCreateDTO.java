package com.example.projeto_a3_sistemas_distribuidos_mobile_api.dto;

public record UserCreateDTO(
    String name,
    String email,
    String password
) {}