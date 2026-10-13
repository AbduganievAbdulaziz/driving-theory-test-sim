package com.abdulaziz.driving_theory.domain;

public record Language (int id, String code, String name) {
    public Language {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Language code is null or empty");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Language name is null or empty");
        }
    }
}
