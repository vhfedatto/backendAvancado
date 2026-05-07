package com.example.frankenstein.domain.service;

import org.springframework.stereotype.Component;

@Component
public class CpfValidator {

    public void validate(String cpf) {
        // Tirei a validação do controller e trouxe para uma regra de domínio mais isolada.
        if (cpf == null || cpf.length() != 11) {
            throw new RuntimeException("CPF Inválido!");
        }
    }
}
